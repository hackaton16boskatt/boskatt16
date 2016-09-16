(ns boskatt16.rsv
  (:require
    [clj-http.client :as client]
    [cheshire.core :refer :all]))

;; anrop av Skatteverkets öppna API

(defn call-api-skatt- [] (client/get "http://skv-hackaton-2016-joesew.c9users.io/taxrate"))

(def skatt (call-api-skatt-))

(def s1 (skatt :body))

(def s2 (cheshire.core/decode s1 true))

(defn extr-vals
  "extract township, municipal and tax value from map"
  [data]
  (select-keys data [:township :municipal :sumExklChuchFee]))

(def s3 (map extr-vals s2))

(defn twochars
  "first two chars of string"
  [s]
  (if s (.substring s 0 2) "dummy") )

(def s4 (map twochars (map :code s2)))

(def s5 (map (fn [x] (assoc {} :nr x)) s4))

(def s6 (map vector s3 s5))

(def s7 (map (fn [v] (merge (get v 0) (get v 1))) s6))

(def s8 (group-by :nr s7))


(defn average
  [numbers]
  (/ (apply + numbers) (count numbers)))

(def s9 (map average (map (fn [v] (map :sumExklChuchFee (get v 1))) s8)))

(def s10 (map (fn [v] (get v 0)) s8))

(def s11 (map vector s9 s10))

(def lan  {:SE-AB "01" :SE-C "03" :SE-D "04" :SE-E "05" :SE-F "06" :SE-G "07" :SE-H "08" :SE-I "09" :SE-K "10" :SE-M "12" :SE-N "13" :SE-O "14" :SE-S "17" :SE-T "18" :SE-U "19" :SE-W "20" :SE-X "21" :SE-Y "22" :SE-Z "23" :SE-AC "24" :SE-BD "25"})

(defn reverse-map
  "Reverse the keys/values of a map"
  [m]
  (into {} (map (fn [[k v]] [v k]) m)))

(def rlan (reverse-map lan))

(def s12 (map (fn [v] (nth v 0)) s11))

(def s13 (map rlan (map (fn [v] (nth v 1)) s11)))

(defn round2
  "Round a double to the given precision (number of significant digits)"
  [d]
  (let [factor (Math/pow 10 1)]
    (/ (Math/round (* d factor)) factor)))

(def s14 (map vector s13 (map str (map round2 s12))))

(def s15 (map (fn [v] (into {} [v])) (vec s14)))

(defn skatt-body [] {:body s15})


