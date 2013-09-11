(ns korn.api
  (:use [hiccup.core :only (html)])
  (:require [korn.routes :as r]
            [shoreleave.middleware.rpc :refer [defremote]]))

(defremote testapi
  []
  (html [:button {:id "test-button" :class "large button"} "wee"]))

(defremote home
  []
  (r/home-content))
  
