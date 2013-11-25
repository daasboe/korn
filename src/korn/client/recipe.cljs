(ns korn.client.recipe
  (:require [enfocus.core :as ef]
            [enfocus.events :as ev]
            [shoreleave.remotes.http-rpc :refer [remote-callback]]
            [korn.validations :as v]
            [korn.client.util :refer [parse-int]])
  (:require-macros [enfocus.macros :as em]))

(declare recipe-index-page
         recipe-template
         list-recipes
         add-to-recipes-table
         new-recipe-page
         new-recipe-template
         recipe-page
         get-recipes
         get-recipe
         create-recipe
         delete-recipe
         validate-recipe-name
         recipe-name-error-handler
         validate-recipe-efficiency
         validate-recipe-batch-size
         validate-field
         validators)

(defn testpage []
  (ef/html [:div "Test"]))

;;;;;;;;;;;;;;
;; Functions (
;;;;;;;;;;;;;;
;; Create (
(defn create-recipe [m]
  (let [v (v/validate-recipe m)]
    (if (empty? v)
      (remote-callback :api/save-recipe [m] #(get-recipe (:id %)))
      (js/console.log (str m " " v)))))
;; )
;; Read (
(defn get-recipes []
  (remote-callback :api/get-recipes [] #(list-recipes %)))

(defn get-recipe [id]
  (remote-callback :api/get-recipe [id] #(recipe-page (first %))))
;; )
;; Update (
;; )
;; Delete (
(defn delete-recipe [id]
  (remote-callback :api/delete-recipe [id] (nav :home)))
;; )
;; )
;;;;;;;;;;;;;;
;; Pages (
;;;;;;;;;;;;;;
;;;; Index(
(defn recipe-index-page []
  (ef/at "#content-pane" (ef/content (ef/html '([:button#testapi {:href "#" :class "button"} "Testapi"] [:table#recipes-table {:style "border:0"} [:tbody]]))))
  (get-recipes))

(defn- recipe-template "Single recipe template" [r]
  (ef/html [:tr {:data-id (:id r)} [:td (:name r)] [:td (:type r)]]))

(defn- list-recipes "list recipes" [rs]
  (do (doseq [r (seq rs)] (add-to-recipes-table r))
      (ef/at "#recipes-table tbody tr" (ev/listen :click #(get-recipe (parse-int (ef/from (.-currentTarget %) (ef/get-attr :data-id))))))))

(em/defaction add-to-recipes-table [recipe]
  "#recipes-table tbody" (ef/append (recipe-template recipe)))
;;;;)
;;;; New (
(defn new-recipe-page []
  (ef/at "#content-pane" (ef/content (new-recipe-template)))
  (ef/at "#new-recipe-form input" (ev/listen :blur #(validate-field %)))
  (ef/at "#btn-create-recipe" (ev/listen :click #(create-recipe (ef/from "#new-recipe-form" (ef/read-form))))))
(defn- new-recipe-template []
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
;;;; Validators (
(defn- validate-recipe-name [m]
  (let [v (v/validate-recipe-name m)] (recipe-name-error-handler v)))
(defn- recipe-name-error-handler [v]
  (if (empty? v)
    (do 
      (ef/at "#recipe-name-row" (ef/remove-class "has-error"))
      (ef/at "#recipe-name-errors" (ef/content "")))
    (do (ef/at "#recipe-name-row" (ef/add-class "has-error"))
        (ef/at "#recipe-name-errors" (ef/content (str (:msg v)))))))
(defn- validate-recipe-efficiency [m]
  (let [v (v/validate-recipe-efficiency m)] (js/console.log (str v))))
(defn- validate-batch-size [m]
  (let [v (v/validate-recipe-batch-size m)] (js/console.log (str v))))
;;;; )
(def validators {:name validate-recipe-name, :efficiency validate-recipe-efficiency, :batch_size validate-batch-size})
(defn- validate-field [t]
  (let [k (keyword (ef/from (.-currentTarget t) (ef/get-attr :name)))
        v (parse-int (ef/from (.-currentTarget t) (ef/get-prop :value)))]
    ((validators k) {k v})))
;;;; )
;;;; Detail (
(defn recipe-page "Detailed recipe page" [r]
  (ef/at "#content-pane" (ef/do-> 
                           (ef/content (str r))
                           (ef/append (ef/html [:button {:id "delete-recipe-button", :class "btn btn-danger"} "Delete"]))))
  (ef/at "#delete-recipe-button" (ev/listen :click #(delete-recipe (:id r)))))
;;;;)
;; )


