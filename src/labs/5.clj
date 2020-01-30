(ns labs.5)

(defn _integrate                                             ; OMG OVERLOADING!!!
  ([f max_x step]
     (reduce +
             (for [x (range 0 max_x step)]
               (* (/ step 2)
                  (+ (f x) (f (+ x step)))
                  )
               )
             )
     )
  )

(defn integrate
  ([f max_x step]
   (_integrate (memoize f) max_x step)
   )
  ([f max_x]
   (integrate f max_x 0.001)
   )
  )


(defn f [x] (* x x))

(println (integrate f 3))
