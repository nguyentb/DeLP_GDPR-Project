package DeLP_GDPR.logics.fol.syntax;




import java.util.*;

import DeLP_GDPR.logics.common.LogicalSymbols;
import DeLP_GDPR.logics.commons.syntax.Functor;
import DeLP_GDPR.logics.commons.syntax.Predicate;
import DeLP_GDPR.logics.commons.syntax.QuantifiedFormulaSupport;
import DeLP_GDPR.logics.commons.syntax.RelationalFormula;
import DeLP_GDPR.logics.commons.syntax.Variable;
import DeLP_GDPR.logics.commons.syntax.interfaces.Term;

/**
 * For-All-quantified first-order logic formula. 
 * Delegates to QuantifiedFormulaSupport for shared functionalities with other quantified formulas.
 * 
 */
public class ForallQuantifiedFormula extends FolFormula {
	
	/**
	 * This helper class implements common functionalities of quantified
	 * formulas, meaning the implementation can delegate the method calls to the support
	 * class. 
	 */
	protected QuantifiedFormulaSupport<FolFormula> support;
	
	/**
	 * Creates a new quantified folFormula with the given folFormula and variables.
	 * @param folFormula the folFormula this quantified folFormula ranges over.
	 * @param variables the variables of this quantified folFormula.
	 */
	public ForallQuantifiedFormula(RelationalFormula folFormula, Set<Variable> variables){
		if(!(folFormula instanceof FolFormula))
			throw new IllegalArgumentException("Formula must be first-order formula.");
		support = new QuantifiedFormulaSupport<FolFormula>((FolFormula) folFormula, variables);
		if(!this.isWellFormed()) throw new IllegalArgumentException("FolFormula not well-formed.");
	}
	
	/**
	 * Creates a new quantified folFormula with the given folFormula and variable.
	 * @param folFormula the folFormula this quantified folFormula ranges over.
	 * @param variable the variable of this quantified folFormula.
	 */
	public ForallQuantifiedFormula(RelationalFormula folFormula, Variable variable){
		if(!(folFormula instanceof FolFormula))
			throw new IllegalArgumentException("Formula must be first-order formula.");
		Set<Variable> variables = new HashSet<Variable>();
		variables.add(variable);
		support = new QuantifiedFormulaSupport<FolFormula>((FolFormula) folFormula, variables);
		if(!this.isWellFormed()) throw new IllegalArgumentException("FolFormula not well-formed.");
	}
	/**
	 * 
	 * @param other another formula
	 */
	public ForallQuantifiedFormula(ForallQuantifiedFormula other) {
		if(!(other.getFormula() instanceof FolFormula))
			throw new IllegalArgumentException("Formula must be first-order formula.");
		support = new QuantifiedFormulaSupport<FolFormula>(other.getFormula(), other.getQuantifierVariables());
		if(!this.isWellFormed()) throw new IllegalArgumentException("FolFormula not well-formed.");
	}
	/**
	 * 
	 * @return the FolFormulas
	 */
	public Set<FolFormula> getQuantifiedFormulas(){
		Set<FolFormula> qf = new HashSet<FolFormula>();
		if(this.getFormula() instanceof AssociativeFolFormula) {
			AssociativeFolFormula af = ((AssociativeFolFormula) this.getFormula());
			qf.addAll(af.getFormulas(ForallQuantifiedFormula.class));
			qf.addAll(af.getFormulas(ExistsQuantifiedFormula.class));
		}
		qf.add(this);
		return qf;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ForallQuantifiedFormula other = (ForallQuantifiedFormula) obj;
		if (this.getFormula() == null) {
			if (other.getFormula() != null)
				return false;
		} else if (!this.getFormula().equals(other.getFormula()))
			return false;
		if (this.getQuantifierVariables() == null) {
			if (other.getQuantifierVariables() != null)
				return false;
		} else if (!this.getQuantifierVariables().equals(other.getQuantifierVariables()))
			return false;
		return true;
	}

	
	@Override
	public FolFormula toNnf() {
		return new ForallQuantifiedFormula( this.getFormula().toNnf(), getQuantifierVariables() );
	}

	 
	  @Override
	  public FolFormula collapseAssociativeFormulas() {
	    return new ForallQuantifiedFormula( (FolFormula) this.getFormula().collapseAssociativeFormulas(), this.getQuantifierVariables() );
	  }


	
	@Override
	public ForallQuantifiedFormula substitute(Term<?> v, Term<?> t) throws IllegalArgumentException{
		if(this.getQuantifierVariables().contains(v))
			return new ForallQuantifiedFormula(this.getFormula(),this.getQuantifierVariables());
		return new ForallQuantifiedFormula(this.getFormula().substitute(v, t),this.getQuantifierVariables());
	}

	
	@Override
	public String toString(){
		String s = LogicalSymbols.FORALLQUANTIFIER() + " ";
		Iterator<Variable> it = this.getQuantifierVariables().iterator();
		if(it.hasNext())
			s += it.next();
		while(it.hasNext())
			s += "," + it.next();
		s += ": (" + this.getFormula() + ")";
		return s;
	}

	@Override
	public ForallQuantifiedFormula clone() {
		return new ForallQuantifiedFormula(this);
	}
	
	//-------------------------------------------------------------------------
	//	METHODS IMPLEMENTED IN QuantifiedFormulaSupport:
	//-------------------------------------------------------------------------
		/**
		 * returns formula
		 */
		public FolFormula getFormula() {
			return support.getFormula();
		}
		/**
		 * returns variables 
		 */
		public Set<Variable> getQuantifierVariables() {
			return support.getQuantifierVariables();
		}
		/**
		 * sets formula
		 * @param formula formula
		 */
		public void setFormula(FolFormula formula) {
			support.setFormula(formula);
		}
		/**
		 * sets vairables
		 * @param variables variables 
		 */
		public void setQuantifierVariables(Set<Variable> variables) {
			support.setQuantifierVariables(variables);
		}
		
		@SuppressWarnings("unchecked")
		@Override
		public Set<FolAtom> getAtoms() {
			return (Set<FolAtom>) support.getAtoms();
		}

		@Override
		public Set<Functor> getFunctors() {
			return support.getFunctors();
		}
		

		@Override
		public boolean isDnf() {
			return support.isDnf();
		}
		

		@Override
		public Set<? extends Predicate> getPredicates() {
			return support.getPredicates();
		}

		@Override
		public boolean isLiteral() {
			return support.isLiteral();
		}

		@Override
		public Set<Variable> getUnboundVariables() {
			return support.getUnboundVariables();
		}

		@Override
		public boolean containsQuantifier() {
			return support.containsQuantifier();
		}

		@Override
		public boolean isWellBound() {
			return support.isWellBound();
		}

		@Override
		public boolean isWellBound(Set<Variable> boundVariables) {
			return support.isWellBound(boundVariables);
		}

		@Override
		public boolean isClosed() {
			return support.isClosed();
		}

		@Override
		public boolean isClosed(Set<Variable> boundVariables) {
			return support.isClosed(boundVariables);
		}

		@Override
		public Set<Term<?>> getTerms() {
			return support.getTerms();
		}

		@Override
		public <C extends Term<?>> Set<C> getTerms(Class<C> cls) {
			return support.getTerms(cls);
		}
}