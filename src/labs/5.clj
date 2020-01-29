(ns labs.5)

(defn integrate                                             ; OMG OVERLOADING!!!
  ([_f max_x step]
   (let [f (memoize _f)]
     (println "Start integrate:")
     (println f max_x step)
     (reduce +
             (for [x (range 0 max_x step)]
               (* (/ step 2)
                  (+ (f x) (f (+ x step)))
                  )
               )
             )
     )
   )
  ([f max_x]
   (integrate f max_x 0.001))
  )


(defn f [x] (* x x))

(println (integrate f 3))
