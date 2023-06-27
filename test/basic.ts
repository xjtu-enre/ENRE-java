/**
 * Defines schema for meta describing single testcase.
 *
 * This a basic schema designed exclusively for languages other than JS/TS.
 * JS/TS should use `raw.ts` rather than this file.
 */
export const schemaObjBasic = {
  type: 'object',
  properties: {
    /**
     * Case's name.
     *
     * Any non-alphabetical character will be converted to `-`.
     */
    name: {type: 'string'},
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
        type: {type: 'string'},
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
            },
            /**
             * Some entities do not possess of location info.
             */
            required: ['name', 'type'],
          },
        },
      },
    },
    relation: {
      type: 'object',
      properties: {
        type: {type: 'string'},
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
            },
            required: ['from', 'to', 'loc'],
          }
        }
      },
    }
  },
  required: ['name'],
};

// TODO: Typing it
export type CaseSchemaBasic = any;
