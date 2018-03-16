/**
 * Copyright 2016, GeoSolutions Sas.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree.
*/

const React = require('react');
const I18N = require('../../MapStore2/web/client/components/I18N/I18N');

const PlatformNumbers = React.createClass({

    propTypes: {
        siradecObject: React.PropTypes.number,
        functionObjectMap: React.PropTypes.number,
        functionObjectSearch: React.PropTypes.number,
        functionObjectView: React.PropTypes.number
    },
    getDefaultProps() {
        return {
            siradecObject: 0,
            functionObjectMap: 0,
            functionObjectSearch: 0,
            functionObjectView: 0
        };
    },

render() {
    return (
        <div className="container-fluid piattaforma">
            <div className="row-fluid">
                <div className="container">
                    <div className="row">
                        <h3><I18N.Message msgId={"PlatformNumbers.PlatformNumbers"}/></h3>
                        <ul className="list-group numeri">
                          <li className="list-group-item col-md-4"><span className="cifra">{this.props.functionObjectMap}</span> <span className="sotto_cifra"><I18N.Message msgId={"PlatformNumbers.view_maps"}/></span></li>
                          <li className="list-group-item col-md-4"><span className="cifra">{this.props.functionObjectSearch}</span> <span className="sotto_cifra"><I18N.Message msgId={"PlatformNumbers.details_search_available"}/></span></li>
                          <li className="list-group-item col-md-4"><span className="cifra">{this.props.functionObjectView}</span> <span className="sotto_cifra"><I18N.Message msgId={"PlatformNumbers.tematic_view_available"}/></span></li>
                        </ul>
                </div>
            </div>
        </div>
        </div>
);
}
});

module.exports = PlatformNumbers;
