(ns korn.client.core
  (:use [cljs.reader :only (read-string)])
  (:require [enfocus.core :as ef]
            [enfocus.events :as ev]
            [shoreleave.remotes.http-rpc :refer [remote-callback]]
            [korn.validations :as v])
  (:require-macros [enfocus.macros :as em]))

(declare home-page
         about-page
         recipe-page
         new-recipe-page
         new-recipe-template
         get-recipes
         get-recipe
         show-recipe
         create-recipe
         delete-recipe
         testapi
         validate-recipe-name
         validators)

(defn navigate [page]
  (fn [] (page)))

(em/defaction setup []
  "#home" (ev/listen :click (navigate home-page))
  "#new" (ev/listen :click (navigate new-recipe-page))
  "#about" (ev/listen :click (navigate about-page)))
  ;".navbar-nav li" (ev/listen :click #(ef/do->
                                        ;(ef/at ".navbar-nav li" (ef/remove-class "active"))
                                        ;(ef/at (.-currentTarget %) (ef/add-class "active")))))

;;;;;;;;;;
;; Pages
(defn home-page []
  (ef/at "#content-pane" (ef/content (ef/html '([:button#testapi {:href "#" :class "button"} "Testapi"] [:table#recipes-table {:style "border:0"} [:tbody]]))))
  (get-recipes)
  (ef/at "#testapi" (ev/listen :click #(testapi)))
  (update-active-nav "#home"))

(defn about-page []
  (ef/at "#content-pane" (ef/content "About-page"))
  (update-active-nav "#about")) 

(defn recipe-page "Detailed recipe page" [r]
  (ef/at "#content-pane" (ef/do-> 
                           (ef/content (str r))
                           (ef/append (ef/html [:button {:id "delete-recipe-button", :class "btn btn-danger"} "Delete"]))))
  (ef/at "#delete-recipe-button" (ev/listen :click #(delete-recipe (:id r)))))

(defn create-recipe [m]
  (let [v (v/validate-recipe m)]
    (if (empty? v)
      (remote-callback :api/save-recipe [m] #(show-recipe (:id %)))
      (js/console.log (str m " " v)))))

(defn delete-recipe [id]
  (remote-callback :api/delete-recipe [id] (navigate home-page)))

;;;;;;;;;;
;; Recipes

;; Create
(defn new-recipe-template []
  (ef/html [:form {:class "form-horizontal", :role "form", :id "new-recipe-form"}

            [:div {:class "form-group", :id "recipe-name-row"}
             [:label {:for "inputName1", :class "col-lg-2 control-label"} "Name"]
             [:div.col-lg-10
              [:input {:id "inputName1", :type "text", :name "name", :class "form-control", :placeholder "name"}]
              [:p.help-block { :id "recipe-name-errors" }]]]

            [:div {:class "form-group"}
             [:label {:for "selectType1", :class "col-lg-2 control-label"} "Type"]
             [:div.col-lg-10
              [:select {:id "selectType1", :name "type", :class "form-control"}
               [:option "Allgrain"]
               [:option "Extract"]
               [:option "Partial"]]]]

            [:div {:class "form-group", :id "efficiency-row"}
             [:label {:for "efficiency", :class "col-lg-2 control-label"} "Efficiency"]
             [:div.col-lg-10
              [:input {:id "efficiency", :type "number", :min "0", :max "100" :name "efficiency", :class "form-control", :value "70"}]
              [:p.help-block { :id "efficiency-errors" }]]]

            [:div {:class "form-group", :id "batch-size-row"}
             [:label {:for "batchSize", :class "col-lg-2 control-label"} "Batch Size"]
             [:div.col-lg-10
              [:input {:id "batchSize", :type "number", :min "0", :max "999" :name "batch_size", :class "form-control", :value "19"}]
              [:p.help-block { :id "efficiency-errors" }]]]

            [:div.form-group
             [:div {:class "col-lg-offset-2 col-lg-10"}
              [:button {:type "button", :class "btn btn-default", :id "btn-create-recipe"} "Create"]]]]))

(defn new-recipe-page []
  (ef/at "#content-pane" (ef/content (new-recipe-template)))
  (ef/at "#new-recipe-form input" (ev/listen :blur #(validate-field %)))
  (ef/at "#btn-create-recipe" (ev/listen :click #(create-recipe (ef/from "#new-recipe-form" (ef/read-form)))))
  (update-active-nav "#new"))

(defn validate-recipe-name [m]
  (let [v (v/validate-recipe-name m)] (recipe-name-error-handler v)))

(defn recipe-name-error-handler [v]
  (if (empty? v)
    (do 
      (ef/at "#recipe-name-row" (ef/remove-class "has-error"))
      (ef/at "#recipe-name-errors" (ef/content "")))
    (do (ef/at "#recipe-name-row" (ef/add-class "has-error"))
        (ef/at "#recipe-name-errors" (ef/content (str (:msg v)))))))

(defn validate-recipe-efficiency [m]
  (let [v (v/validate-recipe-efficiency m)] (js/console.log (str v))))

(defn validate-batch-size [m]
  (let [v (v/validate-recipe-batch-size m)] (js/console.log (str v))))

(def validators {:name validate-recipe-name, :efficiency validate-recipe-efficiency, :batch_size validate-batch-size})

(defn parse-int [i]
  (let [pi (js/parseInt i)]
    (if (integer? pi)
      pi
      i)))

(defn validate-field [t]
  (let [k (keyword (ef/from (.-currentTarget t) (ef/get-attr :name)))
        v (parse-int (ef/from (.-currentTarget t) (ef/get-prop :value)))]
    ;(js/console.log (str k " " v))
    ((validators k) {k v})))

;; Index
(defn recipe-template "Single recipe template" [r]
  (ef/html [:tr {:data-id (:id r)} [:td (:name r)] [:td (:type r)]]))

(em/defaction add-to-recipes-table [recipe]
  "#recipes-table tbody" (ef/append (recipe-template recipe)))

(defn get-recipes []
  (remote-callback :api/get-recipes [] #(list-recipes %)))


(defn list-recipes "list recipes" [rs]
  (do (doseq [r (seq rs)] (add-to-recipes-table r))
      (ef/at "#recipes-table tbody tr" (ev/listen :click #(show-recipe (read-string (ef/from (.-currentTarget %) (ef/get-attr :data-id))))))))

;; Show
(defn show-recipe [id]
  (remote-callback :api/get-recipe [id] #(recipe-page (first %))))

;;;;;;;;;;
;; Testing
(em/defaction add-to-content-pane [content]
  "#content-pane" (ef/append content))

(defn testapi []
  (js/alert (str (v/validate-recipe-name {:name ""}))))

(defn update-active-nav [id]
  (ef/at (str id) #(ef/do->
                    (ef/at ".navbar-nav li" (ef/remove-class "active"))
                    (ef/at (.-parentNode %) (ef/add-class "active")))))

;;;;;;;;;;
;; Init
(defn start []
  (do (new-recipe-page)
      (setup)))

(set! (.-onload js/window) start)
