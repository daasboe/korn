(ns korn.client.core
  (:require [enfocus.core :as ef]
            [enfocus.events :as ev]
            [shoreleave.remotes.http-rpc :refer [remote-callback]])
  (:require-macros [enfocus.macros :as em]))

(declare home-page
         about-page
         recipe-page
         new-recipe-page
         new-recipe-template
         get-recipes
         get-recipe
         testapi)

(defn navigate [page]
  (fn [] (page)))

(em/defaction setup []
  "#home" (ev/listen :click (navigate home-page))
  "#new" (ev/listen :click (navigate new-recipe-page))
  "#about" (ev/listen :click (navigate about-page)))

;;;;;;;;;;
;; Pages
(defn home-page []
  (ef/at "#content-pane" (ef/content (ef/html '([:button#testapi {:href "#" :class "button"} "Testapi"] [:table#recipes-table {:style "border:0"} [:tbody]]))))
  (get-recipes)
  (ef/at "#testapi" (ev/listen :click #(testapi))))

(em/defaction about-page []
  "#content-pane" (ef/content "About-page"))

(defn recipe-page "Detailed recipe page" [id]
  (get-recipe id))

(em/defaction new-recipe-page []
  "#content-pane" (ef/content (new-recipe-template))
  "#btn-create-recipe" (ev/listen :click #(create-recipe (ef/from "#new-recipe-form" (ef/read-form)))))

(defn create-recipe [v]
  (ef/at "#content-pane" (ef/append (apply str (:name v) ": " (first (seq (:type v)))))))

;;;;;;;;;;
;; Recipes

;; Create
(defn new-recipe-template []
  (ef/html [:form {:class "form-horizontal", :role "form", :id "new-recipe-form"}
            [:div {:class "form-group"}
             [:label {:for "inputName1", :class "col-lg-2 control-label"} "Name"]
             [:div.col-lg-10
              [:input {:id "inputName1", :type "text", :name "name", :class "form-control", :placeholder "name", :autofocus ""}]]]
            [:div {:class "form-group"}
             [:label {:for "selectType1", :class "col-lg-2 control-label"} "Type"]
             [:div.col-lg-10
              [:select {:id "selectType1", :name "type", :class "form-control"}
               [:option "Allgrain"]
               [:option "Extract"]
               [:option "Partial"]]]]
            [:div.form-group
             [:div {:class "col-lg-offset-2 col-lg-10"}
              [:button {:type "button", :class "btn btn-default", :id "btn-create-recipe"} "Create"]]]]))

;; Index
(defn recipe-template "Single recipe template" [r]
  (ef/html [:tr {:data-id (:id r)} [:td (:name r)] [:td (:type r)]]))

(em/defaction add-to-recipes-table [recipe]
  "#recipes-table tbody" (ef/append (recipe-template recipe)))

(defn get-recipes []
  (remote-callback :api/get-recipes [] #(list-recipes %)))


(defn list-recipes "list recipes" [rs]
  (do (doseq [r (seq rs)] (add-to-recipes-table r))
      (ef/at "#recipes-table tbody tr" (ev/listen :click #(recipe-page (ef/from (.-currentTarget %) (ef/get-attr :data-id)))))))

;; Show
(defn get-recipe [id]
  (remote-callback :api/get-recipe [id] #(ef/at "#content-pane" (ef/content (str %)))))

;;;;;;;;;;
;; Testing
(em/defaction add-to-content-pane [content]
  "#content-pane" (ef/append content))

(defn testapi []
  (remote-callback :api/testapi [] #(add-to-content-pane (str %))))

;;;;;;;;;;
;; Init
(defn start []
  (do (new-recipe-page)
      (setup)))

(set! (.-onload js/window) start)
