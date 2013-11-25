(ns korn.client.util)

(defn parse-int [i]
  (let [pi (js/parseInt i)]
    (if (integer? pi)
      pi
      i)))

