

free_schedule(_,[],[]).
free_schedule(Alltas, [day(Day,Lts)|Ttdays], [day(Day,L)|Tdaysr]):-
  slotsfree_schedule(Alltas,day(Day,Lts),day(Day,L)),
  free_schedule(Alltas,Ttdays,Tdaysr).


slotsfree_schedule(_,day(_,[]),day(_,[])).
slotsfree_schedule(Alltas, day(Day,[S1|Ts]), day(Day,[R|Tr])):-
 get_allta(Alltas,day(Day,S1),day(Day,R1)),
 permutation(R1,R),
 slotsfree_schedule(Alltas, day(Day,Ts), day(Day,Tr)).
 

 
get_allta([],_,day(_,[])).
get_allta([ta(Name,Doff)|Tta],day(Day,L),day(Day,[Name|T1])):-
Day\=Doff,
\+member(Name,L),

get_allta(Tta,day(Day,L),day(Day,T1)).
get_allta([ta(Name,Doff)|Tta],day(Day,L),FreeTas):-
(Day=Doff
;
member(Name,L)),
get_allta(Tta,day(Day,L),FreeTas).

%took inspiration from extra prolog problems
take_elements(0,_,[]).
take_elements(K,L,[X|Xs]) :- K > 0,
   separate_HandT(X,L,Tail), K1 is K-1, take_elements(K1,Tail,Xs).
  
   
separate_HandT(X,[X|L],L).
separate_HandT(X,[_|L],R) :- separate_HandT(X,L,R).
  
assign_quiz(quiz(_,_,_,_),[],[]).
assign_quiz(quiz(_,D,1,C),[day(D,[S|_])|_],N):-
take_elements(C,S,N1),
permutation(N1,N).

assign_quiz(quiz(_,Day,Slot,Count), [day(Day,[_|T1])|_], N):-
Slot\=1,
Slot1 is Slot-1,
assign_quiz(quiz(_,Day,Slot1,Count), [day(Day,T1)|_], N).

assign_quiz(quiz(_,Day,Slot,Count), [day(Day1,_)|D2], N):-
Day\=Day1,assign_quiz(quiz(_,Day,Slot,Count), D2, N).


assign_quizzes([],_,[]).
assign_quizzes([quiz(Course,Day,Slot,Num)|Tq],Free_Schedule,[proctors(quiz(Course,Day,Slot,Num),At)|Tp]):-
assign_quiz(quiz(Course,Day,Slot,Num),Free_Schedule,At),
assign_quizzes(Tq,Free_Schedule,Tp).

assign_proctors(AllTAs, Quizzes, TeachingSchedule, ProctoringSchedule):-
free_schedule(AllTAs,TeachingSchedule,FreeSchedule),
assign_quizzes(Quizzes,FreeSchedule,ProctoringSchedule).