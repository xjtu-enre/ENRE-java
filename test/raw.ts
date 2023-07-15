/**
 * Defines schema for meta describing single testcase.
 */
export const schemaObj = {
  type: 'object',
  properties: {
    /**
     * Case's name.
     *
     * Any non-alphabetical character will be converted to `-`.
     */
    name: {type: 'string'},
    /**
     * Config fields in package.json
     */
    pkg: {
      type: 'object',
      properties: {
        /**
         * Determine what module system does the file use.
         */
        type: {enum: ['commonjs', 'module']}
      },
      additionalProperties: false,
    },
    /**
     * Assert what errors/warnings should be thrown.
     *
     * // TODO: Decide whether to use error id number, name or full string
     */
    throw: {
      type: 'array',
      uniqueItems: true,
      items: {
        type: 'string',
      }
    },
    /**
     * Defines entity's fetching properties.
     */
    entity: {
      type: 'object',
      properties: {
        /**
         * Set `type` property for all entity items conveniently.
         *
         * If another `type` is presented in an item,
         * that value will override this.
         */
        type: {
          enum: [
              'package',
              'file',
              'class',
              'enum',
              'enum constant',
              'annotation',
              'annotation member',
              'interface',
              'method',
              'module',
              'record',
              'type parameter',
              'variable',
          ],
        },
        /**
         * Whether to allow unlisted entities to exist.
         *
         * Only items without `negative` will be counted.
         *
         * Rules:
         * 1. If `entity.type` is set: no more entities with the explicit `entity.type`, other types are still allowed;
         * 2. If `entity.type` is not set: no more entities other than those in items;
         * 3. Items that `item.negative: true` will always be ignored in any circumstance.
         *
         * @default true
         */
        extra: {type: 'boolean', default: true},
        /**
         * Entities to be validated.
         */
        items: {
          type: 'array',
          uniqueItems: true,
          items: {
            type: 'object',
            properties: {
              /**
               * Entity's name.
               */
              name: {type: 'string'},
              /**
               * Entity's qualified name.
               */
              qualified: {type: 'string'},
              /**
               * Entity's location (String format explained in packages/enre-location).
               */
              loc: {type: 'string'},
              /**
               * Whether it is a negative test item.
               *
               * A negative test item is entity that should NOT be extracted.
               */
              negative: {type: 'boolean', default: false},
              additionalProperties: false,
            },
            required: ['name', 'loc', 'type'],
            oneOf: [
              /**
               * package
               */
              {
                type: 'object',
                properties: {
                  type: {const: 'package'},
                },
              },
              /**
               * File
               */
              {
                type: 'object',
                properties: {
                  type: {const: 'file'},
                },
              },
              /**
               * Class
               */
              {
                type: 'object',
                properties: {
                  type: {const: 'class'},
                },
              },
              /**
               * Enum
               */
              {
                type: 'object',
                properties: {
                  type: {const: 'enum'},
                },
              },
              /**
               * Enum Constant
               */
              {
                type: 'object',
                properties: {
                  type: {const: 'enum constant'},
                },
              },
              /**
               * Annotation
               */
              {
                type: 'object',
                properties: {
                  type: {const: 'annotation'},
                },
              },
              /**
               * Annotation Member
               */
              {
                type: 'object',
                properties: {
                  type: {const: 'annotation member'},
                },
              },
              /**
               * Interface
               */
              {
                type: 'object',
                properties: {
                  type: {const: 'interface'},
                },
              },
              /**
               * Method
               */
              {
                type: 'object',
                properties: {
                  type: {const: 'method'},
                  Lambda: {type: 'boolean'},
                },
              },
              /**
               * Module
               */
              {
                type: 'object',
                properties: {
                  type: {const: 'module'},
                },
              },
              /**
               * Record
               */
              {
                type: 'object',
                properties: {
                  type: {const: 'record'},
                  Modifier: {enum: ['public', 'protected', 'private']},
                },
              },
              /**
               * Type Parameter
               */
              {
                type: 'object',
                properties: {
                  type: {const: 'type parameter'},
                },
              },
              /**
               * Variable
               */
              {
                type: 'object',
                properties: {
                  type: {const: 'variable'},
                  global: {type: 'boolean'},
                  rawType: {type: 'string'},
                },
              },
            ],
          },
        },
      },
      additionalProperties: false,
    },
    relation: {
      type: 'object',
      properties: {
        type: {
          enum: [
            'import',
            'inherit',
            'implement',
            'contain',
            'call',
            'parameter',
            'typed',
            'usevar',
            'set',
            'modify',
            'annotate',
            'cast',
            'override',
            'reflect',
            'define',
          ]
        },
        extra: {type: 'boolean', default: true},
        items: {
          type: 'array',
          uniqueItems: true,
          items: {
            type: 'object',
            properties: {
              from: {type: 'string'},
              to: {type: 'string'},
              loc: {type: 'string'},
              /**
               * Negative relation expects both entities, and the relation does not exist.
               */
              negative: {type: 'boolean', default: false},
              additionalProperties: false,
            },
            required: ['from', 'to', 'loc'],
            oneOf: [
              /**
               * Import
               */
              {
                type: 'object',
                properties: {
                  type: {const: 'import'},
                },
              },
              /**
               * Inherit
               */
              {
                type: 'object',
                properties: {
                  type: {const: 'inherit'},
                },
              },
              /**
               * Implement
               */
              {
                type: 'object',
                properties: {
                  type: {const: 'implement'},
                },
              },
              /**
               * Contain
               */
              {
                type: 'object',
                properties: {
                  type: {const: 'contain'},
                },
              },
              /**
               * Call
               */
              {
                type: 'object',
                properties: {
                  type: {const: 'call'},
                },
              },
              /**
               * Parameter
               */
              {
                type: 'object',
                properties: {
                  type: {const: 'parameter'},
                },
              },
              /**
               * Typed
               */
              {
                type: 'object',
                properties: {
                  type: {const: 'typed'},
                },
              },
              /**
               * UseVar
               */
              {
                type: 'object',
                properties: {
                  type: {const: 'usevar'},
                },
              },
              /**
               * Set
               */
              {
                type: 'object',
                properties: {
                  type: {const: 'set'},
                },
              },
              /**
               * Modify
               */
              {
                type: 'object',
                properties: {
                  type: {const: 'modify'},
                },
              },
              /**
               * Annotate
               */
              {
                type: 'object',
                properties: {
                  type: {const: 'annotate'},
                },
              },
              /**
               * Cast
               */
              {
                type: 'object',
                properties: {
                  type: {const: 'cast'},
                },
              },
              /**
               * Override
               */
              {
                type: 'object',
                properties: {
                  type: {const: 'override'},
                  negative: {type: 'boolean'},
                },
              },
              /**
               * Reflect
               */
              {
                type: 'object',
                properties: {
                  type: {const: 'reflect'},
                },
              },
              /**
               * Define
               */
              {
                type: 'object',
                properties: {
                  type: {const: 'define'},
                },
              },
            ],
          }
        }
      },
      additionalProperties: false,
    }
  },
  required: ['name'],
  additionalProperties: false,
};

// TODO: Typing it
export type CaseSchema = any;
