/**
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree.
 */

const React = require('react');
const {connect} = require('react-redux');
const I18N = require('../../MapStore2/web/client/components/I18N/I18N');
const Dialog = require('../../MapStore2/web/client/components/misc/Dialog');
// const {Button} = require('react-bootstrap');
const {loadLocale} = require('../../MapStore2/web/client/actions/locale');

const LangBar = connect((state) => ({
    currentLocale: state.locale && state.locale.current
}), {
    onLanguageChange: loadLocale.bind(null, null)
})(require('../../MapStore2/web/client/components/I18N/LangBar'));

const ChangeLanguage = React.createClass({
     propTypes: {
         showPanel: React.PropTypes.bool,
         onClosePanel: React.PropTypes.func
     },
     getDefaultProps() {
         return {
             onClosePanel: () => {}
        };
     },

    render() {
         return this.props.showPanel ?
             (
             <Dialog style ={{position: "absolute", right: "410px", top: "190px", backgroundColor: "white", width: "250px"}} className="cartpanel-panel" id="decsiraweb-cartpanel">
                 <span role="header"><span className="cartpanel-panel-title" ><I18N.Message msgId={"ChangeLanguage.language"}/></span><button className="print-panel-close close" onClick={this.props.onClosePanel}><span>×</span></button></span>
                 <div role="body">
                     <LangBar/>
                 </div>
                 <div role="footer" >
                 </div>
             </Dialog>
            ) : null;
     }
 });

module.exports = ChangeLanguage;
