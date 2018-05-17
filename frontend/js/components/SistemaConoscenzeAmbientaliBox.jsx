/**
 * Copyright 2016, GeoSolutions Sas.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree.
 */

const React = require('react');
const {Panel} = require('react-bootstrap');
const Draggable = require('react-draggable');
const I18N = require('../../MapStore2/web/client/components/I18N/I18N');

const SistemaConoscenzeAmbientaliBox = React.createClass({
    propTypes: {
        title: React.PropTypes.string,
        panelStyle: React.PropTypes.object,
        show: React.PropTypes.string,
        closePanel: React.PropTypes.func
    },

    getDefaultProps() {
        return {
            title: 'title',
            show: 'none',
            panelStyle: {
                      height: "500px",
                      width: "550px",
                      zIndex: 100,
                      position: "absolute",
                      overflow: "auto"
                  },
            closePanel: () => {}
        };
    },

    render() {
        return (
          <div className="infobox-container" style={{display: this.props.show}}>
          <Draggable start={{x: 300, y: 100}} handle=".panel-heading,.handle_featuregrid,.handle_featuregrid *">
              <Panel
                  className = "infobox-content toolbar-panel modal-dialog-container react-draggable"
                  style={this.props.panelStyle}
                  header={
                      <span>
                          <span className="snapshot-panel-title">
                              <I18N.Message msgId={"RightMenu.ConoscenzeAmbTitle"}/>
                          </span>
                          <button className="print-panel-close close" onClick={this.props.closePanel}><span>×</span></button>
                      </span>}>
                      <h3>Work in progress ....</h3>
                </Panel>
              </Draggable>
            </div>
          );
    }
});
module.exports = SistemaConoscenzeAmbientaliBox;
