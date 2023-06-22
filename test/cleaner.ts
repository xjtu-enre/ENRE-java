import {promises as fs} from 'fs';

/**
 * Remove old generated files in a given folder,
 * create if it does not exist.
 */
export default async (identifier: string) => {
  // Remove cases
  const groupPath = `tests/cases/_${identifier}`;

  try {
    // Entries can not only be directories (officially), but also files
    const entryList = await fs.readdir(groupPath);

    /**
     * Remove dirs/files whose name starts with _
     */
    for (const caseName of entryList.filter(name => name.charAt(0) === '_')) {
      const casePath = `${groupPath}/${caseName}`;

      try {
        await fs.rm(casePath);
      } catch (e) {
        undefined;
      }
    }
  } catch (e: any) {
    if (e.code === 'ENOENT') {
      /**
       * If the dir does not exist, just create it
       * since it will be used to contain case files later,
       * at when there will be no chance to create non-existing folder.
       *
       * (Create any missing hierarchy)
       */
      await fs.mkdir(groupPath, {recursive: true});
    }
  }

  // Create if suite folder does not exist
  try {
    await fs.readdir('tests/suites');

    // Remove suite
    const suitePath = `tests/suites/_${identifier}.spec.js`;
    try {
      await fs.rm(suitePath);
    } catch (e) {
      undefined;
    }
  } catch (e) {
    await fs.mkdir('tests/suites');
  }
};
