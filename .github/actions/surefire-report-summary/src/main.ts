import * as core from '@actions/core'
import {parse, read} from './reader'
import { Results } from './types';


async function run(): Promise<void> {
  try {
    const filePath:string = core.getInput('file-path', {required: true});
    
    core.debug(`file-path: ${filePath}`);
    const results = await parse( read(filePath) );
    await core.summary.addHeading('Test Results')
    .addTable([
      [{data: 'Tests', header: true}, {data: 'Failures', header: true}, {data: 'Errors', header: true},{data: 'Skipped', header: true}],
      [results.tests.toString(), results.failures.toString(), results.errors.toString(), results.skipped.toString()]
    ])
    .write();
    await failOnTestFailures(results) ? core.setFailed('Test failures found') : core.info('fail-on-test-failures is false, ignoring test failures');
  } catch (error) {
    core.setFailed(`${(error as Error)?.message ?? error}`)
  }
}



export const failOnTestFailures = async (results: Results): Promise<boolean> => {
  const fail_on_test_failures:boolean = core.getInput('fail-on-test-failures') === 'true';
   if (!fail_on_test_failures) {
       return false;
   }
  return results.failures > 0;
}

export default run;