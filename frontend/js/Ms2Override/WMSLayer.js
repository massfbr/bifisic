/**
 * Copyright 2015, GeoSolutions Sas.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree.
 */

var Layers = require('../../MapStore2/web/client/utils/openlayers/Layers');
var ol = require('openlayers');
var objectAssign = require('object-assign');
const CoordinatesUtils = require('../../MapStore2/web/client/utils/CoordinatesUtils');
const ProxyUtils = require('../../MapStore2/web/client/utils/ProxyUtils');
const ConfigUtils = require('../../MapStore2/web/client/utils/ConfigUtils');
const {isArray} = require('lodash');
const SecurityUtils = require('../../MapStore2/web/client/utils/SecurityUtils');
const mapUtils = require('../../MapStore2/web/client/utils/MapUtils');
const securityUtils = require('../../MapStore2/web/client/utils/SecurityUtils');

function wmsToOpenlayersOptions(options) {
    // NOTE: can we use opacity to manage visibility?
    return objectAssign({}, options.baseParams, {
        LAYERS: options.name,
        STYLES: options.style || "",
        FORMAT: options.format || 'image/png',
        TRANSPARENT: options.transparent !== undefined ? options.transparent : true,
        SRS: CoordinatesUtils.normalizeSRS(options.srs || 'EPSG:3857', options.allowedSRS),
        CRS: CoordinatesUtils.normalizeSRS(options.srs || 'EPSG:3857', options.allowedSRS),
        TILED: options.tiled || false,
        VERSION: options.version || "1.3.0"
    }, options.params || {});
}

function getWMSURLs( urls ) {
    return urls.map((url) => url.split("\?")[0]);
}

// Works with geosolutions proxy
function proxyTileLoadFunction(imageTile, src) {
    var newSrc = src;
    if (ProxyUtils.needProxy(src)) {
        let proxyUrl = ProxyUtils.getProxyUrl();
        newSrc = proxyUrl + encodeURIComponent(src);
    }
    imageTile.getImage().src = newSrc;
}

// https://github.com/openlayers/openlayers/issues/4213
function customTileLoader(imageTile, src) {
    let gsUrl = ConfigUtils.getConfigProp("geoserverUrl");
    let tmpUrl = gsUrl.substring(gsUrl.indexOf('://') + 4, gsUrl.length);
    if (src && src.indexOf(tmpUrl) > -1) {
        let proxyUrl = ProxyUtils.getProxyUrl();
        let newSrc = proxyUrl + encodeURIComponent(src);
        let xmlhttp = new XMLHttpRequest();
        xmlhttp.open('GET', newSrc);
        // TODO to take Authorization dinamically from state
	var requestHeader = securityUtils.getBasicAuthHeader();
        xmlhttp.setRequestHeader('Authorization', requestHeader);
	//xmlhttp.setRequestHeader('Authorization', 'Basic YmlmaXNpY3VzZXI6Y25wMTYwNF9ncw==');
	/* FM 12/06/2018         
	https://github.com/openlayers/openlayers/issues/5401
	xmlhttp.onload = function() {
            // const urlCreator = window.URL || window.webkitURL;
            let data = this.responseText;
            data = btoa(encodeURIComponent(data));
            data = "data:image/png;base64, " + data;
            // data = 'data:image/png;base64,' + btoa(unescape(encodeURIComponent(this.responseText)));
            // data = "data:image/png;base64, iVBORw0KGgoAAAANSUhEUgAAAAUAAAAFCAYAAACNbyblAAAAHElEQVQI12P4//8/w38GIAXDIBKE0DHxgljNBAAO    9TXL0Y4OHwAAAABJRU5ErkJggg==";
            imageTile.getImage().src = data;
            // imageTile.getImage().src = urlCreator.createObjectURL(data);
        };
	*/
	xmlhttp.responseType = "arraybuffer";

	xmlhttp.onload = function () {
	    var arrayBufferView = new Uint8Array(this.response);
	    var blob = new Blob([arrayBufferView], { type: 'image/png' });
	    var urlCreator = window.URL || window.webkitURL;
	    var imageUrl = urlCreator.createObjectURL(blob);
	    imageTile.getImage().src = imageUrl;
	};

        xmlhttp.send();
    }else {
        imageTile.getImage().src = src;
    }
}

Layers.registerType('wms', {
    create: (options, map) => {
        const urls = getWMSURLs(isArray(options.url) ? options.url : [options.url]);
        const queryParameters = wmsToOpenlayersOptions(options) || {};
        urls.forEach(url => SecurityUtils.addAuthenticationParameter(url, queryParameters));
        if (options.singleTile) {
            return new ol.layer.Image({
                opacity: options.opacity !== undefined ? options.opacity : 1,
                visible: options.visibility !== false,
                zIndex: options.zIndex,
                source: new ol.source.ImageWMS({
                    url: urls[0],
                    params: queryParameters
                })
            });
        }
        const mapSrs = map && map.getView() && map.getView().getProjection() && map.getView().getProjection().getCode() || 'EPSG:3857';
        const extent = ol.proj.get(CoordinatesUtils.normalizeSRS(options.srs || mapSrs, options.allowedSRS)).getExtent();
        let mSource = new ol.source.TileWMS(objectAssign({
          urls: urls,
          params: queryParameters,
          tileGrid: new ol.tilegrid.TileGrid({
              extent: extent,
              resolutions: mapUtils.getResolutions(),
              tileSize: options.tileSize ? options.tileSize : 256,
              origin: options.origin ? options.origin : [extent[0], extent[1]]
          })
        }, (options.forceProxy) ? {tileLoadFunction: proxyTileLoadFunction} : {}));
        mSource.setTileLoadFunction(customTileLoader);

        let ll = new ol.layer.Tile({
            opacity: options.opacity !== undefined ? options.opacity : 1,
            visible: options.visibility !== false,
            zIndex: options.zIndex,
            source: mSource
        });
        // if (ll && ll.source) ll.source.setTileLoadFunction(customTileLoader);
        return ll;
    },
    update: (layer, newOptions, oldOptions) => {
        if (oldOptions && layer && layer.getSource() && layer.getSource().updateParams) {
            let changed = false;
            if (oldOptions.params && newOptions.params) {
                changed = Object.keys(oldOptions.params).reduce((found, param) => {
                    if (newOptions.params[param] !== oldOptions.params[param]) {
                        return true;
                    }
                    return found;
                }, false);
            } else if (!oldOptions.params && newOptions.params) {
                changed = true;
            }
            let oldParams = wmsToOpenlayersOptions(oldOptions);
            let newParams = wmsToOpenlayersOptions(newOptions);
            changed = changed || ["LAYERS", "STYLES", "FORMAT", "TRANSPARENT", "TILED", "VERSION" ].reduce((found, param) => {
                if (oldParams[param] !== newParams[param]) {
                    return true;
                }
                return found;
            }, false);

            if (changed) {
                layer.getSource().updateParams(objectAssign(newParams, newOptions.params));
            }
        }
    }
});
