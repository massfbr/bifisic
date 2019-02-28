/**
 * Copyright 2016, GeoSolutions Sas.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree.
 */
const FilterUtils = require('../../MapStore2/web/client/utils/FilterUtils');
const SiraUtils = require('./SiraUtils');
const ol = require('openlayers');
const moment = require('moment');

function getWSNameByFeatureName(ftName = "") {
    if (ftName === "" ) return "";
    return ftName.split(':')[0];
}

FilterUtils.toOGCFilterSira = function(ftName, json, version, projection = null, sortOptions = null, hits = false, format = null, nsplaceholder = "fes") {
    // let gjson = null;
    // gjson = json && json.spatialField ? FilterUtils.reProjectGeometry(json.spatialField.geometry) : null;
    // json.spatialField.geometry = gjson;
    json.spatialField.attribute = this.getWorkSpaceNameByLayerName(ftName) ? this.getWorkSpaceNameByLayerName(ftName) + ":geometry" : "geometry";
    let newFilter = this.toOGCFilter(ftName, json, version, sortOptions, hits, format);
    let undefFilter = `<${nsplaceholder}:Filter>undefined</${nsplaceholder}:Filter>`;
    // newFilter = projection ? newFilter.replace("EPSG:3857", "EPSG:31469") : newFilter;
    newFilter = newFilter.replace(undefFilter, '');
    newFilter = nsplaceholder === "ogc" ? newFilter.replace("<ogc:PropertyName>geometria</ogc:PropertyName>", "<ogc:PropertyName>" + getWSNameByFeatureName(ftName) + ":geometria</ogc:PropertyName>") : newFilter;
    return newFilter;
};

FilterUtils.getWorkSpaceNameByLayerName = function(name) {
    if (name && name.indexOf(":") > -2) {
        return name.split(":")[0];
    }
    return null;
};

FilterUtils.toCQLFilterSira = function(json) {
    let filter = this.toCQLFilter(json);
    return filter === "(undefined)" ? "(INCLUDE)" : filter;
};
FilterUtils.getOgcAllPropertyValue = function(featureTypeName, attribute) {
    return `<wfs:GetPropertyValue service="WFS" valueReference='${attribute}'
                version="2.0" xmlns:fes="http://www.opengis.net/fes/2.0"
                xmlns:gml="http://www.opengis.net/gml/3.2"
                xmlns:wfs="http://www.opengis.net/wfs/2.0"
                xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                xsi:schemaLocation="http://www.opengis.net/wfs/2.0 http://schemas.opengis.net/wfs/2.0/wfs.xsd http://www.opengis.net/gml/3.2 http://schemas.opengis.net/gml/3.2.1/gml.xsd">
                    <wfs:Query typeNames="${featureTypeName}"/>
            </wfs:GetPropertyValue>`;
};
FilterUtils.getSLD = function(ftName, json, version, nsplaceholder, nameSpaces) {
    let filter = this.toOGCFilterSira(ftName, json, version, null, false, null, nsplaceholder);
    let sIdx = filter.search( `<${nsplaceholder}:Filter>`);
    if (sIdx !== -1) {
        let eIndx = filter.search( `</wfs:Query>`);
        filter = filter.substr(sIdx, eIndx - sIdx);
    } else {
        filter = '';
    }
    let configName = ftName.indexOf(":") > -1 ? ftName.split(":")[1] : ftName;
    let geometryType = SiraUtils.getConfigOggetti()[configName].geometryType;
    const nameSpacesAttr = Object.keys(nameSpaces).map((prefix) => 'xmlns:' + prefix + '="' + nameSpaces[prefix] + '"').join(" ");

    let result;

    switch(geometryType) {
        case "Point": {
            result = `<StyledLayerDescriptor version="1.0.0"
                    ${nameSpacesAttr}
                    xsi:schemaLocation="http://www.opengis.net/sld StyledLayerDescriptor.xsd" xmlns:ogc="http://www.opengis.net/ogc" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:gml="http://www.opengis.net/gml/3.2" xmlns:gsml="urn:cgi:xmlns:CGI:GeoSciML:2.0" xmlns:sld="http://www.opengis.net/sld" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"><NamedLayer><Name>${ftName}</Name><UserStyle><FeatureTypeStyle><Rule >${filter}<PointSymbolizer><Graphic><Mark><WellKnownName>circle</WellKnownName><Fill><CssParameter name="fill">#0000FF</CssParameter></Fill></Mark><Size>20</Size></Graphic></PointSymbolizer></Rule></FeatureTypeStyle></UserStyle></NamedLayer></StyledLayerDescriptor>`;
            break;
        }
        case "Polygon": {
            result = `<StyledLayerDescriptor version="1.0.0"
                    ${nameSpacesAttr}
                    xsi:schemaLocation="http://www.opengis.net/sld StyledLayerDescriptor.xsd" xmlns:ogc="http://www.opengis.net/ogc" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:gml="http://www.opengis.net/gml/3.2" xmlns:gsml="urn:cgi:xmlns:CGI:GeoSciML:2.0" xmlns:sld="http://www.opengis.net/sld" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"><NamedLayer><Name>${ftName}</Name><UserStyle><FeatureTypeStyle><Rule >${filter}<PolygonSymbolizer><Fill><CssParameter name="fill">#0000FF</CssParameter><CssParameter name="stroke">#0000FF</CssParameter><CssParameter name="fill-opacity">0.4</CssParameter></Fill></PolygonSymbolizer></Rule></FeatureTypeStyle></UserStyle></NamedLayer></StyledLayerDescriptor>`;
            break;
        }
        default:
            result = "";
    }
    return result;
    // return `<StyledLayerDescriptor version="1.0.0"
    //        ${nameSpacesAttr}
    //        xsi:schemaLocation="http://www.opengis.net/sld StyledLayerDescriptor.xsd" xmlns:ogc="http://www.opengis.net/ogc" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:gml="http://www.opengis.net/gml/3.2" xmlns:gsml="urn:cgi:xmlns:CGI:GeoSciML:2.0" xmlns:sld="http://www.opengis.net/sld" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"><NamedLayer><Name>${ftName}</Name><UserStyle><FeatureTypeStyle><Rule >${filter}<PointSymbolizer><Graphic><Mark><WellKnownName>circle</WellKnownName><Fill><CssParameter name="fill">#0000FF</CssParameter></Fill></Mark><Size>20</Size></Graphic></PointSymbolizer></Rule></FeatureTypeStyle></UserStyle></NamedLayer></StyledLayerDescriptor>`;
};

FilterUtils.getFilterByIds = function(ftName, ids, idField, pagination) {
    let filterObj = {
    groupFields: [{id: 1, index: 0, logic: "OR"}],
    filterFields: ids.map((id) => ({ attribute: idField.xpath[0], groupId: 1, operator: "=", value: id, type: 'string'})),
    spatialField: {},
    pagination
    };
    return this.toOGCFilter(ftName, filterObj, "2.0");
};

// not used
FilterUtils.reProjectGeometry = function(geometry) {
    if (!geometry) return "";
    let polyCoords = [];
    geometry.coordinates[0].forEach((element) => {
        polyCoords.push(ol.proj.transform([parseFloat(element[0]), parseFloat(element[1])], 'EPSG:3857', 'EPSG:31469'));
    });
    let feature = new ol.Feature({
        geometry: new ol.geom.Polygon([polyCoords])
    });
    let jsonGeometry = {};
    if (!feature) return null;
    jsonGeometry.extent = feature.getGeometry().getExtent();
    jsonGeometry.center = [
        ((jsonGeometry.extent[2] - jsonGeometry.extent[0]) / 2) + jsonGeometry.extent[0],
        ((jsonGeometry.extent[3] - jsonGeometry.extent[1]) / 2) + jsonGeometry.extent[3]
    ];
    jsonGeometry.coordinates = feature.getGeometry().getCoordinates();
    jsonGeometry.projection = "EPSG:31469";
    jsonGeometry.type = geometry.type;
    return jsonGeometry;
};

var ogcComparisonOperators = {
    "=": {startTag: "<fes:PropertyIsEqualTo>", endTag: "</fes:PropertyIsEqualTo>"},
    ">": {startTag: "<fes:PropertyIsGreaterThan>", endTag: "</fes:PropertyIsGreaterThan>"},
    "<": {startTag: "<fes:PropertyIsLessThan>", endTag: "</fes:PropertyIsLessThan>"},
    ">=": {startTag: "<fes:PropertyIsGreaterThanOrEqualTo>", endTag: "</fes:PropertyIsGreaterThanOrEqualTo>"},
    "<=": {startTag: "<fes:PropertyIsLessThanOrEqualTo>", endTag: "</fes:PropertyIsLessThanOrEqualTo>"},
    "<>": {startTag: "<fes:PropertyIsNotEqualTo>", endTag: "</fes:PropertyIsNotEqualTo>"},
    "><": {startTag: "<fes:PropertyIsBetween>", endTag: "</fes:PropertyIsBetween>"},
    "like": {startTag: "<fes:PropertyIsLike matchCase=\"true\" wildCard=\"*\" singleChar=\".\" escapeChar=\"!\">", endTag: "</fes:PropertyIsLike>"},
    "ilike": {startTag: "<fes:PropertyIsLike matchCase=\"false\" wildCard=\"*\" singleChar=\".\" escapeChar=\"!\">  ", endTag: "</fes:PropertyIsLike>"},
    "isNull": {startTag: "<fes:PropertyIsNull>", endTag: "</fes:PropertyIsNull>"}
};
var propertyTagReference = {
    "ogc": {startTag: "<ogc:PropertyName>", endTag: "</ogc:PropertyName>"},
    "fes": {startTag: "<fes:ValueReference>", endTag: "</fes:ValueReference>"}
};
    FilterUtils.ogcDateField = function(attribute, operator, value, nsplaceholder) {
        let fieldFilter;
        if (operator === "><") {
            if (value.startDate && value.endDate) {
                fieldFilter =
                            ogcComparisonOperators[operator].startTag +
                                propertyTagReference[nsplaceholder].startTag +
                                    attribute +
                                propertyTagReference[nsplaceholder].endTag +
                                "<" + nsplaceholder + ":LowerBoundary>" +
                                    "<" + nsplaceholder + ":Literal>" + value.startDate.toLocaleDateString()/*toISOString()*/ + "</" + nsplaceholder + ":Literal>" +
                                "</" + nsplaceholder + ":LowerBoundary>" +
                                "<" + nsplaceholder + ":UpperBoundary>" +
                                    "<" + nsplaceholder + ":Literal>" + value.endDate.toLocaleDateString()/*.toISOString()*/ + "</" + nsplaceholder + ":Literal>" +
                                "</" + nsplaceholder + ":UpperBoundary>" +
                            ogcComparisonOperators[operator].endTag;
            }
        } else {
            if (value.startDate) {
                fieldFilter =
                            ogcComparisonOperators[operator].startTag +
                                propertyTagReference[nsplaceholder].startTag +
                                    attribute +
                                propertyTagReference[nsplaceholder].endTag +
                                "<" + nsplaceholder + ":Literal>" + moment(value.startDate).format('YYYY-MM-DD') + "</" + nsplaceholder + ":Literal>" +
                            ogcComparisonOperators[operator].endTag;
            }
        }
        return fieldFilter;
    };

module.exports = FilterUtils;
