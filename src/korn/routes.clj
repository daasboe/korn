(ns korn.routes
  (:use compojure.core
        [hiccup.core :only (html)]
        [hiccup.page :only (html5 include-css include-js)]))

(defn common
  "Layout template"
  [title & body]
  (html5 [:head
         [:title title]
         (include-css "/foundation/css/normalize.css"
                      "/foundation/css/foundation.css")]
        [:body
         [:div {:id "topnav"}
          [:p "TOPNAV"]
          [:a {:id "home" :class "small button"} "HOME"]]
         [:div {:id "container" :class "container"} body]]))

(defn home-content
  []
  (html [:h1 "HOME PAGE"]
        [:button {:id "test-button" :class "button"} "test"]
        (include-js "/js/korn.js")
        [:script "korn.core.init()"]))

(defn home []
  (common "Korn"
          (home-content)))

(defroutes home-routes
  (GET "/" [] (home)))
