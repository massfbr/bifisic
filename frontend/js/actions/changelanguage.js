/**
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree.
 */
const SHOW_LANGAUAGE_PANEL = 'SHOW_LANGAUAGE_PANEL';
const HIDE_LANGAUAGE_PANEL = 'HIDE_LANGAUAGE_PANEL';

function showLanguagePanel() {
    return {
        type: SHOW_LANGAUAGE_PANEL
    };
}

function hideLanguagePanel() {
    return {
        type: HIDE_LANGAUAGE_PANEL
    };
}

module.exports = {
    SHOW_LANGAUAGE_PANEL,
    HIDE_LANGAUAGE_PANEL,
    showLanguagePanel,
    hideLanguagePanel
};
