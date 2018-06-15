/**
 * Copyright 2015, GeoSolutions Sas.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree.
 */
var url = require('url');

let supportedLocales = {
     "it": {
         code: "it-IT",
         description: "Italiano"
     },
     "en": {
        code: "en-US",
        description: "English"
     },
     "fr": {
       code: "fr-FR",
       description: "Français"
     },
     "de": {
       code: "de-DE",
       description: "Deutsch"
     },
     "hr": {
       code: "hr-HR",
       description: "Hrvatski"
     }
};

var LocaleUtils = {
    ensureIntl(callback) {
        require.ensure(['intl', 'intl/locale-data/jsonp/en.js', 'intl/locale-data/jsonp/it.js', 'intl/locale-data/jsonp/fr.js', 'intl/locale-data/jsonp/de.js', 'intl/locale-data/jsonp/hr.js'], (require) => {
            global.Intl = require('intl');
            require('intl/locale-data/jsonp/en.js');
            require('intl/locale-data/jsonp/it.js');
            require('intl/locale-data/jsonp/fr.js');
            require('intl/locale-data/jsonp/de.js');
            require('intl/locale-data/jsonp/hr.js');
            if (callback) {
                callback();
            }
        });
    },
    setSupportedLocales: function(locales) {
        supportedLocales = locales;
    },
    normalizeLocaleCode: function(localeCode) {
        var retval;
        if (localeCode === undefined || localeCode === null) {
            retval = undefined;
        } else {
            let rg = /^[a-z]+/i;
            let match = rg.exec(localeCode);
            if (match && match.length > 0) {
                retval = match[0].toLowerCase();
            } else {
                retval = undefined;
            }
        }
        return retval;
    },
    getUserLocale: function() {
        return LocaleUtils.getLocale(url.parse(window.location.href, true).query);
    },
    getLocale: function(query) {
        let locale = supportedLocales[
            LocaleUtils.normalizeLocaleCode(query.locale || (navigator ? navigator.language || navigator.browserLanguage : "hr"))
        ];
        return locale ? locale.code : "hr-HR";
    },
    getSupportedLocales: function() {
        return supportedLocales;
    },
    getMessageById: function(messages, msgId) {
        var message = messages;
        msgId.split('.').forEach(part => {
            message = message ? message[part] : null;
        });
        return message;
    }
};

module.exports = LocaleUtils;
