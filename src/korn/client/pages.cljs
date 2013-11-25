(ns korn.client.pages
  (:require [enfocus.core :as ef]
            [enfocus.events :as ev]))

(defn about-page []
  (ef/at "#content-pane" (ef/content "About page")))

(defn page-not-found []
  (ef/at "#content-pane" (ef/content "Page not found...")))
