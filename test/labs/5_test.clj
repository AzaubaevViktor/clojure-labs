(ns labs.5-test
  (:require [clojure.test :as test])
  (:use labs.5))


(defn abs [n] (max n (-' n)))

(defn square [x] (* x x))
(defn sin [x] (Math/sin x))

(test/deftest simple-test
  (test/testing "Ho Ho Ho IEEE754 is awesome"
    (test/is (< (abs (- 9 (integrate square 3))) 0.01))
    (test/is (< (abs (- 243 (integrate square 9))) 0.01))
    (test/is (< (abs (- 2 (integrate sin Math/PI))) 0.01))
    (test/is (< (abs (- 1 (integrate sin (/ Math/PI 2)))) 0.01))

    )
  )

(simple-test)


(defn super_sin [x] (sin (sin (sin (* x (sin x))))))
(defn super_puper_sin [x] (super_sin (* (super_sin x) (super_sin (* x (sin x))))))

(println (time (_integrate super_puper_sin Math/PI 0.00001)))
(println (time (integrate super_puper_sin Math/PI 0.00001)))
