/**
 * Allowed types for `Anonymous` entity name.
 */
export const ENRENameAnonymousTypes = [
  'Function',
  'ArrowFunction',
  'Class',
  'CallableSignature',
  'NumberIndexSignature',
  'StringIndexSignature',

  // To support cross-tester
  'Namespace',
  'Package',
  'Struct',
  'Union',
  'Enum',
  'Variable',
  'Method'
] as const;

export type ENRENameAnonymousType = typeof ENRENameAnonymousTypes[number];

/**
 * An object representing properties needed by an anonymous entity.
 */
export type ENRENameAnonymous = {
  /**
   * Indicates in which type of anonymous entity this is.
   */
  readonly as: ENRENameAnonymousType;
}
