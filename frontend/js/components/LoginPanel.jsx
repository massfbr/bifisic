/**
 * Copyright 2016, GeoSolutions Sas.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree.
 */

const React = require('react');
const {connect} = require('react-redux');
const {Modal} = require('react-bootstrap');
const {login, setUser} = require('../actions/userprofile');
const I18N = require('../../MapStore2/web/client/components/I18N/I18N');
// const M2LoginPanel = require('../../MapStore2/web/client/components/security/forms/LoginForm');

const M2LoginPanel = connect((state) => ({
    showSubmitButton: true,
    user: state.userprofile.user,
    loginError: state.userprofile.error
}), {
    onSubmit: login,
    onLoginSuccess: setUser
})(require('../../MapStore2/web/client/components/security/forms/LoginForm'));

const LoginPanel = React.createClass({

    propTypes: {
        showLoginPanel: React.PropTypes.bool,
        onClosePanel: React.PropTypes.func,
        onConfirm: React.PropTypes.func
    },

    getDefaultProps() {
        return {
            showLoginPanel: false,
            onClosePanel: () => {},
            onConfirm: () => {}
        };
    },

    renderLoginForm() {
        return this.props.showLoginPanel ? (<M2LoginPanel />) : '';
    },

    render() {
        return (
            <div className="infobox-container" style={{display: this.props.showLoginPanel}}>
            <Modal
                    show= {this.props.showLoginPanel}>
                    <Modal.Header closeButton onClick={this.props.onClosePanel}>
                        <Modal.Title><I18N.Message msgId={"LoginPanel.LoginTitle"}/></Modal.Title>
                    </Modal.Header>
                    <Modal.Body>
                        {this.renderLoginForm()}
                    </Modal.Body>
                    <Modal.Footer />
                </Modal>
            </div>
        );
    }

});

module.exports = LoginPanel;
