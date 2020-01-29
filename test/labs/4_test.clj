(ns labs.4-test
  (:use labs.4)
  (:require [clojure.test :as test])
  )

(def abc2_result (set '((\a, \b), (\a, \c), (\b, \a), (\b, \c), (\c, \a), (\c, \b))) )
(def abc3_result (set '((\a, \b, \c), (\a, \c, \b),
                        (\b, \a, \c), (\b, \c, \a),
                        (\c, \a, \b), (\c, \b, \a))))


(test/deftest simple-test
  (test/testing "Hey hey this is the test!"
    (test/is (= (gen_sequence "abc" -1) '()))
    (test/is (= (gen_sequence "abc" 0) '()))
    (test/is (= (gen_sequence "abc" 1) '((\a), (\b), (\c))))
    (test/is (= (set (gen_sequence "abc" 2)) abc2_result))
    (test/is (= (set (gen_sequence "abc" 3)) abc3_result))
    (test/is (= (gen_sequence "abc" 4) '()))

    (test/is (= (set (gen_sequence "abcabcabccba" 2)) abc2_result))
    (test/is (= (set (gen_sequence "abcabcabccba" 3)) abc3_result))
    (test/is (= (gen_sequence "abcabcabccba" 0) '()))
    (test/is (= (gen_sequence "abcabcabccba" 5) '()))

    (test/is (= (set (gen_sequence "aaabbbccc" 2)) abc2_result))

    )
  )

(simple-test)