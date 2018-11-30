/**
 * Copyright 2018, CSI Piemonte.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree.
 */

const React = require('react');

const GridCellToExponential = React.createClass({
    propTypes: {
        params: React.PropTypes.object.isRequired
    },
    render() {
        return this.props.params.value!=null && this.props.params.colDef.toExponential!=null ? (<span title={this.props.params.value}>{Number.parseFloat(this.props.params.value.replace(',','.')).toExponential(this.props.params.colDef.toExponential)}</span>) : (<noscript/>);
    }
});

module.exports = GridCellToExponential;
