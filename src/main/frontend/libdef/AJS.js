declare var AJS: {

  I18n: {
    getText: (key: string, ...args?: string[]) => string
  },
  toInit: (initFunc: () => any) => void,
  params: {
    baseUrl: string
  }
};
