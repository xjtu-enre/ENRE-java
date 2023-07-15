import parser from "@enre/doc-parser";
import finder from "@enre/doc-path-finder";
import {promises as fs} from 'fs';
import {entityNameMap, relationNameMap} from './java/JUnitBuilder';
import path from 'node:path';

interface LocationDTO {
  startLine?: number;
  startColumn?: number;
  endLine?: number;
  endColumn?: number;
}

interface EntityDTO {
  name: string;
  type: string | undefined;
  qualifiedName?: string;
  File: string;
  location: LocationDTO;
  isLambda?: boolean;
  Modifier?: string;
  global?: boolean;
  rawType?: string;
  negative?: boolean;
  extra?: boolean;
}

function convertEntity(schemaObj: any) {
  let res: EntityDTO = <any>{};
  res["name"] = schemaObj["name"]["printableName"];
  res["type"] = entityNameMap.get(schemaObj["type"]);
  if (res["type"] == "Method") {
    res["isLambda"] = schemaObj["Lambda"];
  } else if (res["type"] == "Record") {
    res["Modifier"] = schemaObj["Modifier"];
  } else if (res["type"] == "Variable") {
    res["global"] = schemaObj["global"];
    res["rawType"] = schemaObj["rawType"];
  }
  if ("qualified" in schemaObj) {
    res["qualifiedName"] = schemaObj["qualified"];
  }
  if ("negative" in schemaObj) {
    res["negative"] = schemaObj["negative"];
  }
  if ("extra" in schemaObj) {
    res["extra"] = schemaObj["extra"];
  }
  if ("loc" in schemaObj) {
    const sloc = schemaObj["loc"];
    let loc: LocationDTO = <any>{};
    if ("file" in sloc) {
      res["File"] = sloc["file"];
    }
    if ("start" in sloc) {
      loc["startLine"] = sloc["start"]["line"];
      loc["startColumn"] = sloc["start"]["column"];
    }
    if ("end" in sloc) {
      loc["endLine"] = sloc["end"]["line"];
      loc["endColumn"] = sloc["end"]["column"];
    }
    res["location"] = loc;
  }
  return res;
}

function convertRelation(schemaObj: any) {
  schemaObj["type"] = relationNameMap.get(schemaObj["type"]);
  return schemaObj;
}

export default async function(opt: any) {
  await parser(
    await finder(opt),
    async (entry, groupMeta) => {
    },
    undefined,
    async (entry, caseObj, groupMeta) => {
      let entities: Array<any> = [];
      let cells: Array<any> = [];
      let res = {
        "variables": entities,
        "cells": cells,
      };
      caseObj.assertion.entity.items.forEach((x: any) => {
        entities.push(convertEntity(x));
      });
      caseObj.assertion.relation?.items?.forEach((x: any) => {
        cells.push(convertRelation(x));
      });
      let filename = `${caseObj.assertion.name}.json`;
      let filepath = path.join("groundtruth", groupMeta.name);
      try {
        await fs.mkdir(filepath, {
          recursive: true,
        });
      } catch (error) {
        console.error(error);
      }
      filepath = path.join(filepath, filename);
      try {
        await fs.writeFile(filepath, JSON.stringify(res, null, 2));
      } catch (error) {
        console.error(error);
      }
    },
    /java/,
    'java',
    false,
  );
}
