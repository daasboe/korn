(ns korn.client.recipe
  (:use [cljs.reader :only (read-string)])
  (:require [enfocus.core :as ef]
            [enfocus.events :as ev]
            [shoreleave.remotes.http-rpc :refer [remote-callback]]
            [korn.validations :as v])
  (:require-macros [enfocus.macros :as em]))
