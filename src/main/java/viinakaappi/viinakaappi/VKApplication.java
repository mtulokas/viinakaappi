package viinakaappi.viinakaappi;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import spark.Spark;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import spark.ModelAndView;
import spark.Spark;
import spark.template.thymeleaf.ThymeleafTemplateEngine;
import viinakaappi.viinakaappi.database.Database;
import viinakaappi.viinakaappi.dao.RaakaaineDao;
import viinakaappi.viinakaappi.domain.Raakaaine;
import viinakaappi.viinakaappi.dao.DrinkkiDao;
import viinakaappi.viinakaappi.domain.Drinkki;
import viinakaappi.viinakaappi.dao.DrinkkiRaakaaineDao;

public class VKApplication {

    public static void main(String[] args) throws Exception {
        Database database = new Database("jdbc:sqlite:db/kanta.db");
        RaakaaineDao raakaaineet = new RaakaaineDao(database);
        DrinkkiDao drinkit = new DrinkkiDao(database);
        DrinkkiRaakaaineDao drinkkiRaakaaineet = new DrinkkiRaakaaineDao(database);

        Spark.get("/", (req, res) -> {
            HashMap map = new HashMap<>();
            return new ModelAndView(map, "index");
        }, new ThymeleafTemplateEngine());

        Spark.get("/raakaaine", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("raakaaineet", raakaaineet.findAll());
            return new ModelAndView(map, "raakaaine");
        }, new ThymeleafTemplateEngine());

        Spark.post("/raakaaine", (req, res) -> {
            HashMap map = new HashMap<>();
            Raakaaine ra = new Raakaaine(0, req.queryParams("nimi"));
            raakaaineet.saveOrUpdate(ra);
            res.redirect("/raakaaine");
            return "";
        });

        Spark.get("/raakaaine/:id", (req, res) -> {
            Integer id = Integer.parseInt(req.params(":id"));
            raakaaineet.delete(id);
            res.redirect("/raakaaine");
            System.out.println(id);
            return "";
        });

        Spark.get("/drinkit", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("drinkit", drinkit.findAll());
            return new ModelAndView(map, "drinkit");
        }, new ThymeleafTemplateEngine());

        Spark.post("/drinkit", (req, res) -> {
            HashMap map = new HashMap<>();
            Drinkki dr = new Drinkki(0, req.queryParams("nimi"));
            drinkit.saveOrUpdate(dr);
            res.redirect("/drinkit");
            return "";
        });
        Spark.post("/drinkit/:id", (req, res) -> {
            Integer id = Integer.parseInt(req.params(":id"));
            System.out.println("Poistetaan: " + id);
            drinkkiRaakaaineet.delete(id);
            drinkit.delete(id);
            res.redirect("/drinkit");
            return "";
        });

        Spark.get("/drinkki/:id", (req, res) -> {
            HashMap map = new HashMap<>();
            Integer id = Integer.parseInt(req.params(":id"));
            map.put("drinkki", drinkit.findOne(id));
            map.put("raakaaineet", raakaaineet.findAll());
            map.put("drinkinRaakaaineet", drinkkiRaakaaineet.findAllByDrinkkiId(id));
            return new ModelAndView(map, "drinkki");
        }, new ThymeleafTemplateEngine());

        Spark.post("/drinkki/:id", (req, res) -> {
            Integer drinkkiId = Integer.parseInt(req.params(":id"));
            Integer raakaineId = Integer.parseInt(req.queryParams("raakaaineId"));
            String maara = req.queryParams("maara");
            System.out.println("drinkkiId: " + drinkkiId);
            System.out.println("raakaaineId: " + raakaineId);
            System.out.println("maara: " + maara);
            drinkkiRaakaaineet.insertRow(drinkkiId, raakaineId, maara);
            res.redirect("/drinkki/" + drinkkiId);
            return "";
        });

        Spark.post("/drinkki/:id/:raakaaineId", (req, res) -> {
            System.out.println("Haloo!");
            Integer drinkkiId = Integer.parseInt(req.params(":id"));
            Integer raakaineId = Integer.parseInt(req.params(":raakaaineId"));
            String param = req.queryParams("param");
            System.out.println(param);
            if (param.equals("moveUp")) {
                drinkkiRaakaaineet.moveUp(drinkkiId, raakaineId);
            } else if (param.equals("delete")) {
                drinkkiRaakaaineet.delete(drinkkiId, raakaineId);
            }
            res.redirect("/drinkki/" + drinkkiId);
            return "";
        });

    }
}
