#lang plai

(define-type Binding
  [binding (name symbol?) (named-expr WAE?)])
 
(define-type WAE
  [num (n number?)]
  [binop (op procedure?)
         (lhs WAE?)
         (rhs WAE?)]
  [with (lob (listof Binding?)) (body WAE?)]
  [id (name symbol?)])


(define op-table
  (list (list '+ +)
        (list '- -)
        (list '* *)
        (list '/ /)))


;lookup-op? : symbol -> procedure or false
;given a symbol, if it represents one of our four operators, return
;             the procedure associated with it, else return false
(define (lookup-op op)
  (if (assoc op op-table) (first (cdr (assoc op op-table))) #f))

(test (lookup-op '+) +)
(test (lookup-op '-) -)
(test (lookup-op '*) *)
(test (lookup-op '/) /)
(test (lookup-op 'a) #f)
(test (lookup-op -) #f)
(test (lookup-op #f) #f)



; parse? : s-exp -> WAE
; to convert s-expressions into WAEs
(define (parse s-exp)
  (cond
    [(number? s-exp) (num s-exp)]
    [(symbol? s-exp)
     (cond
       [(lookup-op s-exp) (error "Illegal syntax")]
       [(equal? s-exp 'with) (error "Illegal syntax")]
       [else (id s-exp)])]
    [(list? s-exp)
     (cond
       [(empty? s-exp) (error "Illegal syntax")]
       [(equal? (first s-exp) 'with)
        (cond
          [(not (list? (second s-exp))) (error "Illegal syntax")]
          [(not (equal? (length s-exp) 3)) (error "Illegal syntax")]
          [else
             (if (andmap (lambda (x)
                        (cond
                          [(not (list? x)) #f]
                          [(not (equal? (length x) 2)) #f]
                          [(not (symbol? (first x))) #f]
                          [else  (binding (first x)
                                  (parse (second x)))]))(second s-exp))
                 (with (map (lambda (x)
                        (if (equal? (length x) 2)
                                (binding (first x)
                                 (parse (second x)))
                                  #f)) (second s-exp))
                       (parse (third s-exp)))
                 (error "Illegal syntax"))])]
       [else
          (cond
            [(equal? (length s-exp) 3) 
            (binop (lookup-op (first s-exp))
                   (parse (second s-exp))
                   (parse (third s-exp)))]
            [else (error "Illegal syntax")])])]
    [else (error "Illegal syntax")]))



(test (parse '5) (num 5))
(test/exn (parse "hello") "Illegal syntax")
(test/exn (parse true) "Illegal syntax")
(test (parse '(+ 1 2)) (binop + (num 1) (num 2)))
(test (parse '(- 1 2)) (binop - (num 1) (num 2)))
(test (parse '(* 1 2)) (binop * (num 1) (num 2)))
(test (parse '(/ 1 2)) (binop / (num 1) (num 2)))
(test/exn (parse '(+ 5)) "Illegal syntax")
(test/exn (parse '(+ 1 2 3)) "Illegal syntax")
(test (parse '(with ([x 1] [x 2]) (+ x x)))
      (with (list (binding 'x (num 1))
                  (binding 'x (num 2)))
            (binop + (id 'x) (id 'x))))
(test/exn (parse '(with ([x 5]))) "Illegal syntax")
(test/exn (parse '(with ([x 5]) (+ 1 x) (+ 2 x))) "Illegal syntax")
(test/exn (parse '(with x (+ 1 x) (+ 2 x))) "Illegal syntax")
(test/exn (parse '(with (x 5) (+ 1 x) (+ 2 x))) "Illegal syntax") 
(test/exn (parse '(with ((x)) (+ 1 x))) "Illegal syntax")
(test/exn (parse '(with ((x 5 6)) (+ 1 x))) "Illegal syntax")
(test/exn (parse '(with ((42 5)) (+ 1 2))) "Illegal syntax")
(test/exn (parse '(with [x 1] x)) "Illegal syntax")
(test (parse 'a) (id 'a))
(test/exn (parse '+) "Illegal syntax")
(test/exn (parse '-) "Illegal syntax")
(test/exn (parse '*) "Illegal syntax")
(test/exn (parse '/) "Illegal syntax")
(test/exn (parse 'with) "Illegal syntax")
(test/exn (parse '()) "Illegal syntax")



;subst*? : list(bindings), WAE -> WAE
;Substitutes for all of the bindings in lob inside body simultaneously
                ;i.e mutates one WAE into another via substitution 
(define (subst* lob body)
  (foldl subst body lob))

;subst? : binding, WAE -> WAE
;workhorse for subst*.  does substitution for one variable in the WAE
(define (subst bind body)
  (cond
    [(num? body) body]
    [(binop? body) (binop (binop-op body)
                          (subst bind (binop-lhs body))
                          (subst bind (binop-rhs body)))]
    [(with? body) (subst bind (subst* (with-lob body) (with-body body)))]
    [(id? body) (if (symbol=? (binding-name bind) (id-name body))       
                        (binding-named-expr bind) body)]))



(test (subst* (list (binding 'x (num 1))) (id 'x))
      (num 1))
(test (subst* (list (binding 'x (num 1))
                    (binding 'y (num 2)))
              (binop + (id 'x) (id 'y)))
      (binop + (num 1) (num 2)))
(test (subst* (list (binding 'x (num 2)))
          (with (list (binding 's (num 1))) (binop + (id 's) (id 'x))))
       (binop + (num 1) (num 2)))




;calc? WAE → number
;Consumes a WAE representation of an expression and computes
;the corresponding numerical result, eagerly.
(define (calc e)
  (type-case WAE e
    [num (n) n]
    [binop (op l r)
           (if  (and (eq? op /) (eq? (num-n r) 0))
                (error "division by zero")
                (op (calc l) (calc r)))]
    [with (lob  body) (calc (subst* lob body))]
    [id (v) (error "free identifier")]))

;multiple-bindings? list(bindings) -> boolean
;a helper function for calc to look for multiple bindings
;(define (multiple-bindings lob)
     
(test (calc (parse '5)) 5)      
(test (calc (parse '(+ 4 1))) 5)
(test (calc (parse '(- 4 1))) 3)
(test (calc (parse '(* 4 1))) 4)
(test (calc (parse '(/ 4 2))) 2)
(test/exn (calc (parse '(/ 4 0))) "division by zero")
(test (calc (parse '(with ([x 5]) x))) 5)
(test/exn (calc (parse 'x)) "free identifier")
(test/exn (calc (parse '(+ 4 w))) "free identifier")
(test (calc (parse '(with ([x 5]) (+ x 5)))) 10)
(test (calc (parse '(with ([x 5]) (with ([x 6]) (+ x 5))))) 11)
(test (calc (parse '(with ([x 5]) (with ([x (+ x 1)]) (+ x 5))))) 11)
;(test/exn (calc (parse '(with ([x 1] [x 2]) (+ x x))))
 ;         "Multiple bindings")
















