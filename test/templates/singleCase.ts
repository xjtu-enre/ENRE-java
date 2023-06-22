import template from '@babel/template';

export default template.smart(`describe(%%name%%, () => {
  beforeAll(async () => {
      await usingCore(%%path%%);
    });
    afterAll(() => {
      cleanAnalysis();
    });

  %%tests%%
});
`);
