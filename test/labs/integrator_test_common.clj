(ns labs.integrator-test-common
  (:require [clojure.test :as test])
)


(defn abs [n] (max n (-' n)))

(defn square [x] (* x x))
(defn sin [x] (Math/sin x))

(defn super-sin [x] (sin (sin (sin (* x (sin x))))))
(defn super-puper-sin [x] (super-sin (* (super-sin x) (super-sin (* x (sin x))))))


(defn do-tests [integrator]
  (test/deftest simple-test
    (test/testing "Ho Ho Ho IEEE754 is awesome"
      (test/is (< (abs (- 9 (integrator square 3))) 0.01))
      (test/is (< (abs (- 243 (integrator square 9))) 0.01))
      (test/is (< (abs (- 2 (integrator sin Math/PI))) 0.01))
      (test/is (< (abs (- 1 (integrator sin (/ Math/PI 2)))) 0.01))

      )
    )

  (println "Tests:")

  (time (simple-test))

  (println "Time tests:")

  (time (println (integrator super-puper-sin 10)))
  (time (println (integrator super-puper-sin 10)))
  (time (println (integrator super-puper-sin 15)))
  (time (println (integrator super-puper-sin 15)))

  )
