/**
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree.
*/
const assign = require('object-assign');

const {SHOW_LANGAUAGE_PANEL, HIDE_LANGAUAGE_PANEL} = require('../actions/changelanguage');

const initialState = {
  // showCart: true,
  showLanguagePanel: false
};

function changelanguage(state = initialState, action) {
    switch (action.type) {
        case SHOW_LANGAUAGE_PANEL: {
            return assign({}, state, {showLanguagePanel: true});
        }
        case HIDE_LANGAUAGE_PANEL: {
            return assign({}, state, {showLanguagePanel: false});
        }
        default:
            return state;
    }
}

module.exports = changelanguage;
