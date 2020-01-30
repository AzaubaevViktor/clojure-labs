(ns labs.7)

; Constant

(defn constant [bool]
  {:pre [(boolean? bool)]}
  (list ::const bool)
  )

(defn constant? [expr]
  (= (first expr) ::const)
  )

(defn constant-value [c]
  {:pre [(constant? c)]}
  (second c)
  )

; Variable

(defn variable [name]
  {:pre [(keyword? name)]}
  (list ::var name)
  )

(defn variable? [expr]
  (= (first expr) ::var)
  )

(defn variable-name [v]
  (second v)
  )

(defn same [v1 v2]
  {:pre [(and (variable? v1) (variable? v2))]}
  (=
    (variable-name v1)
    (variable-name v2)
    )
  )

; Invert

(defn invert [expr]
  (list ::inv expr)
  )

(defn invert? [expr]
  (= (first expr) ::inv)
  )

(defn sub [expr]
  {:pre [invert? expr]}
  (second expr)
  )

; Conjunct

(defn conjunct [expr & rest]
  (cons ::conj (cons expr rest))
  )

(defn conjunct? [expr]
  (= (first expr) ::conj)
  )

; Disjunct

(defn disjunct [expr & rest]
  (cons ::disj (cons expr rest))
  )

(defn disjunct? [expr]
  (= (first expr) ::disj)
  )

(defn parts [expr]
  {:pre [(or
           (conjunct? expr)
           (disjunct? expr)
           )]}
  (rest expr)
  )

; Implicat

(defn implicat [expr_left expr_right]
  (cons ::impl (list expr_left expr_right))
  )

(defn implicat? [expr]
  (= (first expr) ::impl)
  )

(defn left [expr]
  {:pre [(implicat? expr)]}
  (second expr)
  )

(defn right [expr]
  {:pre [(implicat? expr)]}
  (nth expr 2)
  )

; Rules helpers

(defn id [_] _)

; Rules

(declare dnf)

(def bool-rules
  (list
    ; Constant
    [constant? id]
    ; Variable
    [variable?
     id
     ]
    ; Invert
    [invert?
     (fn [expr]
       (let [subexpr (sub expr)]
         (cond
           (constant? subexpr) expr
           (variable? subexpr) expr
           (invert? subexpr) (dnf (sub subexpr))
           ; Invert conj/disj
           (disjunct? subexpr) (dnf
                                 (apply conjunct (map
                                                   #(dnf (invert %))
                                                   (parts subexpr)
                                                   )))

           (conjunct? subexpr) (dnf
                                 (apply disjunct (map
                                                   #(dnf (invert %))
                                                   (parts subexpr)
                                                   )))
           ; ¬ (A => B)  ~~~  ¬(¬A V B) ~~~ A /\ ¬B
           (implicat? subexpr) (dnf
                                 (conjunct                  ; Simplify =>
                                   (left subexpr)
                                   (invert (right subexpr))
                                   )
                                 )

           ))
       )
     ]
    ; Implicat --
    [implicat? (fn [expr]
                 (dnf (disjunct                            ; Simplify =>
                        (invert (left expr))
                        (right expr)
                        ))
                 )
     ]
    ; Disjunct
    [disjunct?
     (fn [expr]
       (apply disjunct (mapcat #(if (disjunct? %)           ; A or (B or C) is A or B or C ; Simplify
                                  (map (fn [arg] (dnf arg)) (parts %) )
                                  (list (dnf %))
                                  )
                               (parts expr))) )]
    [conjunct?
     (fn [expr]
       (let [flattened (mapcat #(if (conjunct? %)
                                  (map (fn [arg] (dnf arg)) (parts %) )
                                  (list (dnf %))
                                  )
                               (parts expr) )
             found_disjunction (first (filter (fn [arg] (disjunct? arg)) flattened) )
             ]
         (if (nil? found_disjunction)
           flattened
           ; A and (B or C) is A and B or A and C  ; Simplify
           (apply disjunct
                  (map
                    (fn [arg]
                      (apply conjunct (cons arg (filter
                                                  (fn [conjunction] (not (identical? conjunction found_disjunction)) )
                                                  flattened
                                                  )))
                      )
                    (parts found_disjunction))
                  )
           )
         )
       )]

    )
  )

(defn wrong [expr]
  (println "Oh bleat, something goes wrong" expr))


(defn dnf [expr]
  ((some (fn [[check, action]]
           ;(println expr, check, (check expr), action)
           (if (check expr)
             action
             false))
         bool-rules)
   expr))


(defn expr_type [expr] (first expr))


(defn check_eq [expr1 expr2]
  (println "Checking equality of" expr1 expr2)
  (if (=
        (expr_type expr1)
        (expr_type expr2)
        )

    (cond
            (constant? expr1) (=
                                (constant-value expr1)
                                (constant-value expr2)
                                )
            (variable? expr1) (=
                                (variable-name expr1)
                                (variable-name expr2)
                                )
            (invert? expr1) (check_eq
                              (sub expr1)
                              (sub expr2)
                              )
            (implicat? expr1) (and
                                (check_eq
                                  (left expr1)
                                  (left expr1))
                                (check_eq
                                  (left expr1)
                                  (left expr1))
                                )
            ;(or (disjunct? expr1) (conjunct? expr1)) (println "fff") ; Add
            )
    false
    )
  )