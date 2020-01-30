(ns labs.7-test
  (:require [clojure.test :as test])
  (:use labs.7)
  )


(def c_true (constant true))
(def c_false (constant false))


(test/deftest check_tests
  (test/testing "Check check lol"
    (test/is (check_eq c_true c_true))
    (test/is (check_eq c_false c_false))
    (test/is (not (check_eq c_true c_false)))
    (test/is (not (check_eq c_false c_true)))

    (test/is (check_eq
               (disjunct (constant true) (constant false))
               (disjunct (constant true) (constant false))
               ))

    )
  )

(test/deftest dnf-high-level
  (test/testing "Very high test man"
    (test/is (check_eq (dnf c_true) c_true))
    (test/is (check_eq (dnf (invert c_true)) c_false))      ; FIXME!
    )
  )

(check_tests)
(dnf-high-level)

(println (dnf (invert c_true)))
(println (dnf (variable :x)))

(let [e (invert (implicat
                  c_true
                  c_false
                  ))]
  (println e)
  (println (dnf e)))

(println "================================")

(let [e (implicat
          (variable :A)
          (variable :B)
          )]
  (println e)
  (println (dnf e)))
