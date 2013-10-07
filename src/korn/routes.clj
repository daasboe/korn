(ns korn.routes
  (:use compojure.core
        [hiccup.core :only (html)]
        [hiccup.page :only (html5 include-css include-js)]))

(defn common
  "Layout template"
  [title & body]
  (html5 [:head
          [:title title]
          [:meta {:name "viewport", :content "width=device-width, initial-scale=1.0"}]
          (include-css "/css/bootstrap.min.css"
                       "/css/custom.css")]
         [:body
          [:nav {:class "navbar navbar-default", :role "navigation"}
           [:div.navbar-header
            [:button {:type "button", :class "navbar-toggle", :data-toggle "collapse", :data-target ".navbar-top-collapse"}
             [:span {:class "sr-only"} "Toggle navigation"]
             [:span {:class "icon-bar"}]
             [:span {:class "icon-bar"}]
             [:span {:class "icon-bar"}]]
            [:a {:class "navbar-brand", :href "#"} "Korn"]]
           [:div {:class "collapse navbar-collapse navbar-top-collapse"}
            [:ul {:class "nav navbar-nav"}
             [:li {:class "active"}
              [:a {:id "home" :class ""} "HOME"]]
             [:li
              [:a {:id "new" :class ""} "NEW"]]
             [:li [:a {:id "about" :class ""} "ABOUT"]]
             [:li.dropdown
              [:a {:href "#", :class "dropdown-toggle", :data-toggle "dropdown"} "Dropdown"]
              [:ul.dropdown-menu
               [:li [:a {:href "#"} "Test1"]]
               [:li [:a {:href "#"} "Test2"]]
               [:li [:a {:href "#"} "Test3"]]]]]]]
          [:div {:id "content-pane" :class "container"} body]
          (include-js "//code.jquery.com/jquery.js"
                      "/js/bootstrap.min.js"
                      "/js/korn.js")]))

(defn home []
  (common "Korn" "Javascript needed to run application..."))

(defroutes home-routes
  (GET "/" [] (home)))
