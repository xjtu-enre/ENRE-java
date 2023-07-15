import {Command} from 'commander';
import handler from './java';

const cli = new Command();

cli
  .description('generate test cases and suites from docs/entity/* or docs/relation/*\nleaving option empty will process all')
  .option('-e --entity [name...]',
    'specify scope names in docs/entity\nleaving name empty will process all files under docs/entity')
  .option('-r --relation [name...]',
    'specify scope names in docs/relation\nleaving name empty will process all files under docs/relation')
  .action(handler);

cli.parse(process.argv);
