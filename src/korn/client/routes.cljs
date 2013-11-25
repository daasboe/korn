(ns korn.client.routes
  (:require [enfocus.core :as ef]
            [enfocus.events :as ev]
            [korn.client.pages :as pages]
            [korn.client.recipe :as recipe]))

(declare update-active-nav
         page
         nav)

(defn update-active-nav [token]
  (let [id (clojure.string/replace (str token) #":" "#" )]
    (ef/at id #(ef/do->
                 (ef/at ".navbar-nav li" (ef/remove-class "active"))
                 (ef/at (.-parentNode %) (ef/add-class "active"))))))

(def page {:about pages/about-page
           :new   recipe/new-recipe-page
           :home  recipe/recipe-index-page
           :404   pages/page-not-found})

(defn nav [token]
  (if (contains? page token)
    (do
      ((page token))
      (update-active-nav token))
    ((page :404))))
