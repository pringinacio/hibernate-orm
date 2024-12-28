/*
 * SPDX-License-Identifier: LGPL-2.1-or-later
 * Copyright Red Hat Inc. and Hibernate Authors
 */
package org.hibernate.query;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

/**
 * A compound restriction constructed using logical OR.
 *
 * @param restrictions The restrictions to be OR-ed
 * @param <X> The entity type
 *
 * @author Gavin King
 */
record Disjunction<X>(java.util.List<? extends Restriction<? super X>> restrictions)
		implements Restriction<X> {
	@Override
	public Restriction<X> negated() {
		return new Conjunction<>( restrictions.stream().map( Restriction::negated ).toList() );
	}

	@Override
	public Predicate toPredicate(Root<? extends X> root, CriteriaBuilder builder) {
		return builder.or( restrictions.stream()
				.map( restriction -> restriction.toPredicate( root, builder ) )
				.toList() );
	}
}
