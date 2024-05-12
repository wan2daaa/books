package wane.cleanarchicture.application.port.in;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import wane.cleanarchicture.common.SelfValidating;
import wane.cleanarchicture.domain.Account;
import wane.cleanarchicture.domain.Money;

import java.util.Objects;

import static java.util.Objects.requireNonNull;

@Getter
@EqualsAndHashCode(callSuper = false)
public class SendMoneyCommand extends SelfValidating<SendMoneyCommand> {

	@NotNull
	private final Account.AccountId sourceAccountId;
	@NotNull
	private final Account.AccountId targetAccountId;
	@NotNull
	private final Money money;

	public SendMoneyCommand(Account.AccountId sourceAccountId, Account.AccountId targetAccountId, Money money) {
		this.sourceAccountId = sourceAccountId;
		this.targetAccountId = targetAccountId;
		this.money = money;
		if (money.isNegative()) {
			throw new IllegalStateException("Money must be positive");
		}
		this.validateSelf();
	}
}
