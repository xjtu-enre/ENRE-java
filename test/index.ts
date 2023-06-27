import {buildENREName} from '@enre/naming';
import {ajv, toUrlFriendlyName} from '../common';
import entityRefMetaParser from '../entity-ref-meta';
import locMetaParser from '../loc-meta';
import {schemaObj} from './raw';
import {schemaObjBasic} from './basic';

export default (meta: any, useBasic = false) => {
  let validator;
  if (useBasic) {
    validator = ajv.compile(schemaObjBasic);
  } else {
    validator = ajv.compile(schemaObj);
  }

  let typeInitializer = meta.entity?.type;

  if (meta.entity && meta.entity.type) {
    meta.entity["type"] = meta.entity.type.toLowerCase();
  }

  // Pre-fulfill fields that are not part of the schema
  // Fulfill `type` for entity.items
  for (const ent of meta.entity?.items || []) {
    if (typeInitializer && !ent.type) {
      ent.type = typeInitializer;
    }
    ent.type = ent.type.toLowerCase();
  }

  if (meta.relation && meta.relation.type) {
    meta.relation["type"] = meta.relation.type.toLowerCase();
  }
  // Fulfill `type` for relation.items
  typeInitializer = meta.relation?.type;
  for (const rel of meta.relation?.items || []) {
    if (typeInitializer && !rel.type) {
      rel.type = typeInitializer;
    }
    rel.type = rel.type.toLowerCase();
  }

  // After preprocessing, validate cases by ajv
  const valid = validator(meta);

  if (!valid) {
    throw validator.errors;
  }

  // After validating, convert string representations to objects
  for (const ent of (meta as any).entity?.items || []) {
    ent.name = buildENREName(ent.name);
    ent.loc = locMetaParser(ent.loc, ent.name);
    ent.declarations ? ent.declarations = ent.declarations.forEach((d: string) => locMetaParser(d)) : undefined;
  }

  for (const rel of (meta as any).relation?.items || []) {
    rel.from = entityRefMetaParser(rel.from);
    rel.to = entityRefMetaParser(rel.to);
    rel.loc = locMetaParser(rel.loc);

    if (rel.src || rel.dest) {
      throw 'You are using legacy syntax \'src\' and \'dest\', please use \'from\' and \'to\' instead';
    }
  }

  // After validating, convert name to url-friendly-name
  meta.name = toUrlFriendlyName(meta.name as string);

  return meta;
};
