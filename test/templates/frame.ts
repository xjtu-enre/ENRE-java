import template from '@babel/template';

export default template.program(`/* *****************************************************************************
This file is auto generated, any changes made here will be DISCARDED
the next time pretest script run.

To modify a test case, or add some new tests that may make documentation
redundant, either
    edit the source in the correlated documentation block,
or
    create a new case file and corresponding suite file, whose name
    started WITHOUT a backslash(_), these kinds of file will be record by
    git, of course you should add them to git manually first.

Please DO NOT MODIFY this file.
***************************************************************************** */

import {eGraph, rGraph} from '@enre/container';
import usingCore, {cleanAnalysis} from '@enre/core';
import {buildFullLocation, expandENRELocation} from '@enre/location';

%%body%%
`, {
  preserveComments: true
});
