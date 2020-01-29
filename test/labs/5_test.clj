(ns labs.5-test
  (:require [clojure.test :as test])
  (:use labs.5))


(defn abs [n] (max n (-' n)))

(defn square [x] (* x x))
(defn sin [x] (Math/sin x))

(test/deftest simple-test
  (test/testing "Ho Ho Ho IEEE754 is awesome"
    (test/is (< (abs (- 9 (integrate square 3))) 0.01))
    (test/is (< (abs (- 81 (integrate square 9))) 0.01))
    (test/is (< (abs (- 1 (integrate sin Math/PI))) 0.01))
    (test/is (< (abs (- 0 (integrate sin (/ Math/PI 2)))) 0.01))

    )
  )