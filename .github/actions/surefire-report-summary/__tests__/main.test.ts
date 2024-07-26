import {expect, test, jest, beforeEach, describe} from '@jest/globals'
import * as core from '@actions/core'
import * as run from '../src/main';
import { parse } from '../src/reader';


jest.mock('../src/reader');
jest.mock('@actions/core');

    beforeEach(() => {
        jest.clearAllMocks();
      });
describe('main', () => {
    test('should call parse and set output', async () => {
        const filePath = 'path/to/file.xml';
        const results = {
            tests: 2,
            failures: 1,
            errors: 1,
            skipped: 0
        };
        (parse as jest.Mock).mockReturnValue(new Promise((resolve) => resolve(results)));   
        (core.getInput as jest.Mock).mockReturnValueOnce(filePath);
        await run.default();
        expect(core.setOutput).toHaveBeenCalledWith('results', results);
    })
    test('should handle error', async () => {
        const filePath = 'path/to/file.xml';
        const error = new Error('Invalid XML format');
        (parse as jest.Mock).mockReturnValue(new Promise((_, reject) => reject(error)));
        (core.getInput as jest.Mock).mockReturnValueOnce(filePath);
        await run.default();
        expect(core.setFailed).toHaveBeenCalledWith(error.message);
    })
    
})



