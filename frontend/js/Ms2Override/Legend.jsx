const urlUtil = require('url');
const React = require('react');
const {isArray} = require('lodash');

const SecurityUtils = require('../../MapStore2/web/client/utils/SecurityUtils');
const ConfigUtils = require('../../MapStore2/web/client/utils/ConfigUtils');
const ProxyUtils = require('../../MapStore2/web/client/utils/ProxyUtils');

const assign = require('object-assign');

const Legend = React.createClass({
    propTypes: {
        layer: React.PropTypes.object,
        legendHeigth: React.PropTypes.number,
        legendWidth: React.PropTypes.number,
        legendOptions: React.PropTypes.string,
        imageUrl: React.PropTypes.oneOfType([ React.PropTypes.string, React.PropTypes.object])
    },
    getDefaultProps() {
        return {
            legendHeigth: 12,
            legendWidth: 12,
            legendOptions: "forceLabels:on;fontSize:10",
            imageUrl: ""
        };
    },

   renderLegendCustom(url) {
       var xmlhttp = new XMLHttpRequest();
       let requestHeader = SecurityUtils.getBasicAuthHeader();
       let gsUrl = ConfigUtils.getConfigProp("geoserverUrl");
       let tmpUrl = gsUrl.substring(gsUrl.indexOf('://') + 4, gsUrl.length);
       // remove :80 if needed...
       let newSrc = url.replace(":80", "");
       newSrc = newSrc.replace(":443", "");
       if (this.imageUrl === "" && newSrc && newSrc.indexOf(tmpUrl) > -1) {
           let proxyUrl = ProxyUtils.getProxyUrl();
           newSrc = proxyUrl + encodeURIComponent(url);
           xmlhttp = new XMLHttpRequest();
           xmlhttp.open('GET', newSrc);
           xmlhttp.setRequestHeader('Authorization', requestHeader);
           xmlhttp.responseType = "arraybuffer";
           let me = this;
           xmlhttp.addEventListener("load", function() {
               me.updateUrl(xmlhttp, "load");
           });
           xmlhttp.send();
       }else {
           this.imageUrl = this.imageUrl === "" ? url : this.imageUrl;
       }
   },

   render() {
       if (this.props.layer && this.props.layer.type === "wms" && this.props.layer.url) {
           let layer = this.props.layer;
           const url = isArray(layer.url) ?
               layer.url[Math.floor(Math.random() * layer.url.length)] :
               layer.url.replace(/[?].*$/g, '');

           let urlObj = urlUtil.parse(url);
           let query = assign({}, {
               service: "WMS",
               request: "GetLegendGraphic",
               format: "image/png",
               height: this.props.legendHeigth,
               width: this.props.legendWidth,
               layer: layer.name,
               style: layer.style || null,
               version: layer.version || "1.3.0",
               SLD_VERSION: "1.1.0",
               LEGEND_OPTIONS: this.props.legendOptions
               // SCALE TODO
           }, layer.legendParams || {},
           layer.params || {},
           layer.params && layer.params.SLD_BODY ? {SLD_BODY: layer.params.SLD_BODY} : {});
           SecurityUtils.addAuthenticationParameter(url, query);

           let legendUrl = urlUtil.format({
               host: urlObj.host,
               protocol: urlObj.protocol,
               pathname: urlObj.pathname,
               query: query
           });
           this.renderLegendCustom(legendUrl);
           return <img src={this.imageUrl} style={{maxWidth: "100%"}}/>;
       }
       return null;
   },

   imageUrl: "",

   updateUrl(xhr) {
       let arrayBufferView = new Uint8Array(xhr.response);
       let blob = new Blob([arrayBufferView], { type: 'image/png' });
       let urlCreator = window.URL || window.webkitURL;
       this.imageUrl = urlCreator.createObjectURL(blob);
   }
});
module.exports = Legend;
