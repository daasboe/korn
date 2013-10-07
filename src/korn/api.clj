(ns korn.api
  (:use [hiccup.core :only (html)]
        [ korma.core])
  (:require [korn.routes :as r]
            [korn.models.db :as db]
            [shoreleave.middleware.rpc :refer [defremote]]))

(defremote testapi []
  (db/get-recipes))
  
(defremote get-recipes []
  (db/get-recipes))
  
(defremote get-recipe [id]
  (db/get-recipe id))
  
