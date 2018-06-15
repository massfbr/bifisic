/**
 * Copyright 2016, GeoSolutions Sas.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree.
 */

var React = require('react');
var Node = require('../../../MapStore2/web/client/components/TOC/Node');
var Title = require('../../../MapStore2/web/client/components/TOC/fragments/Title');
const {Glyphicon, Tooltip, OverlayTrigger} = require('react-bootstrap');
const DefaultGroup = require('../../../MapStore2/web/client/components/TOC/DefaultGroup');
const glyphStyle = {"float": "right", cursor: 'pointer'};
var DefaultNode = React.createClass({
    propTypes: {
        node: React.PropTypes.object,
        settings: React.PropTypes.object,
        expandFilterPanel: React.PropTypes.func,
        onToggle: React.PropTypes.func,
        toggleSiraControl: React.PropTypes.func,
        style: React.PropTypes.object,
        groups: React.PropTypes.array,
        addToMap: React.PropTypes.func,
        showInfoBox: React.PropTypes.func
    },
    getDefaultProps() {
        return {
            style: {},
            expandFilterPanel: () => {},
            onToggle: () => {},
            toggleSiraControl: () => {},
            addToMap: () => {}
        };
    },
    renderTools() {
        let tooltipSira = <Tooltip id="tpm-search-details"><I18N.Message msgId={"DefaultNode.tooltipSira"}/></Tooltip>;
        let tooltipMap = <Tooltip id="tpm-add-map"><I18N.Message msgId={"DefaultNode.tooltipMap"}/></Tooltip>;
        let tooltipList = <Tooltip id="tpm-list-obj"><I18N.Message msgId={"DefaultNode.tooltipList"}/></Tooltip>;
        const tools = [
        (<OverlayTrigger key={"map-tp"} rootClose placement="left" overlay={tooltipMap}>
            <Glyphicon
            style={glyphStyle}
            key="addToMap"
            glyph="plus-sign"
            onClick={()=>this.props.addToMap(this.props.node)}/>
        </OverlayTrigger>)
        ];
        if ( this.props.node.featureType) {
            tools.push((
                <OverlayTrigger key={"sira-mtp"} rootClose placement="left" overlay={tooltipList}>
                        <Glyphicon
                        style={glyphStyle}
                        key="toggle-featuregrid"
                        glyph="th"
                        onClick={() => this.props.toggleSiraControl(this.props.node)}/>
                </OverlayTrigger>));
            tools.push((
                <OverlayTrigger key={"list-ltp"} rootClose placement="left" overlay={tooltipSira}>
                    <Glyphicon
                    style={glyphStyle}
                    key="toggle-query"
                    glyph="search"
                    onClick={() => this.props.expandFilterPanel(true, this.props.node.featureType)}/>
                </OverlayTrigger>));
        }
        return tools;
    },
    render() {
        let {children, onToggle, ...other } = this.props;
        if (this.props.node.nodes) {
            return (
            <DefaultGroup node={this.props.node} animateCollapse={false} onToggle={this.props.onToggle}>
                <DefaultNode {...this.props}/>
            </DefaultGroup>
                );
        }
        return (
            <Node animateCollapse={false} className="toc-default-layer catalog-object" style={this.props.style} type="layer" {...other}>
                <Title onClick={this.props.showInfoBox}/>
                {this.renderTools()}
            </Node>
        );
    }
});

module.exports = DefaultNode;
