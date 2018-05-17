/**
 * Copyright 2016, GeoSolutions Sas.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree.
 */

const React = require('react');
const I18N = require('../../MapStore2/web/client/components/I18N/I18N');

const Footer = React.createClass({

  propTypes: {
  },

  getDefaultProps() {
      return {
      };
  },

    render() {
        return (
        <footer className="footer" role="contentinfo">
            <div className="container-fluid">
                <div className="custom footerCsi row-fluid">
                    <div className="container">
                        <div className="row">
                            <div className="col-md-4 footer-sx">
                                <a href=""><img alt="conoscenze ambientali" src="assets/application/conoscenze_ambientali/css/images/img_void.png" /></a>
                            </div>
                            <div className="col-md-4 text-center">
                                <a href="#"><I18N.Message msgId={"footer.cookie_policy"}/></a>
                            </div>
                            <div className="col-md-4 footer-dx">
                                <a href="#"><img alt="HAOP" src="assets/application/conoscenze_ambientali/css/images/img_void.png" /></a>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </footer>
        );
    }
});
module.exports = Footer;
