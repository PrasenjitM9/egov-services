import { Router } from "express";
import producer from "../kafka/producer";

export default ({ config, db }) => {
  let api = Router();
  api.post("/_create", function({ body }, res) {
    let payloads=[];
    payloads.push({
      topic:"save-fn-firenoc",
      messages:JSON.stringify(body)
    })
    console.log("before",payloads);
    producer.send(payloads, function(err, data) {
      console.log(err);
      console.log(data);
      res.json(data);
    });
  });
  return api;
};
