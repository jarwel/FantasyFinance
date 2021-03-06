package com.fantasystocks.model;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.base.Function;
import com.google.common.base.Objects;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.parse.GetCallback;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

@ParseClassName("Portfolio")
public class Portfolio extends ParseObject {

	public ParseUser getUser() {
		return getParseUser("user");
	}

	public void setUser(ParseUser user) {
		put("user", user);
	}

	public Pool getPool() {
		return (Pool) getParseObject("pool");
	}

	public void setPool(Pool pool) {
		put("pool", pool);
	}

	public double getCash() {
		return getDouble("cash");
	}

	public void setCash(double cash) {
		put("cash", cash);
	}

	public double getStartingFunds() {
		return getDouble("startingFunds");
	}

	public void setStartingFunds(double startingFunds) {
		put("startingFunds", startingFunds);
	}

	public List<Lot> getLots() {
		List<Lot> lots = getList("lots");
		return Objects.firstNonNull(lots, Lists.<Lot> newArrayList());
	}

	public Set<String> getSymbols() {
		return Sets.newHashSet(Iterables.transform(getLots(), new Function<Lot, String>() {
			@Override
			public String apply(Lot lot) {
				return lot.getSymbol();
			}
		}));
	}

	public Double getCurrentValue(Map<String, Quote> quotes) {
		double value = getCash();
		for (Lot lot : getLots()) {
			Quote quote = quotes.get(lot.getSymbol());
			if (quote == null) {
				return null;
			}
			value += lot.getShares() * quote.getPrice();
		}
		return value;
	}

	public void addLot(final String symbol, final int shares, final double costBasis, final SaveCallback callback) {
		fetchIfNeededInBackground(new GetCallback<Portfolio>() {
			@Override
			public void done(Portfolio portfolio, ParseException parseException) {
				if (parseException != null) {
					callback.done(parseException);
				}
				final Lot lot = new Lot();
				lot.setPortfolio(portfolio);
				lot.setSymbol(symbol);
				lot.setShares(shares);
				lot.setCostBasis(costBasis);
				lot.saveInBackground(new SaveCallback() {
					@Override
					public void done(ParseException parseException) {
						if (parseException != null) {
							callback.done(parseException);
						}
						add("lots", lot);
						saveInBackground(callback);
					}
				});
			}
		});
	}

}
