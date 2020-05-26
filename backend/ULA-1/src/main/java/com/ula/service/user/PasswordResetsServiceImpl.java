package com.ula.service.user;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ula.domain.model.PasswordResets;
import com.ula.domain.model.User;
import com.ula.domain.repository.PasswordResetsRepository;
import com.ula.dto.model.PasswordResetsDTO;
import com.ula.dto.model.UserDTO;
import com.ula.security.SecurityConstants;
import com.ula.service.exception.EmailNotFoundException;
import com.ula.service.exception.PasswordsDontMatchException;
import com.ula.service.exception.ResetPasswordTokenInvalidException;
import com.ula.service.exception.UserException;
import com.ula.util.RandomTokenGenerator;

@Service
public class PasswordResetsServiceImpl implements PasswordResetsService
{

	@Autowired
	private UserServiceImpl userService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private PasswordResetsRepository passwordResetsRepository;

	@Transactional
	@Override
	public String sendResetEmail(PasswordResetsDTO passwordResetDTO) throws EmailNotFoundException
	{
		
		Optional<User> user;
		try
		{
			user =  userService.getByEmail(passwordResetDTO.getEmail());
		} catch (UserException e)
		{
			throw new EmailNotFoundException("User with given email does not exist");
		}
		/*
		 * Make an email from template and send it to given email Use worker
		 */
		String token = RandomTokenGenerator.generate(128);

		LocalDate days = LocalDate.now().plusDays(SecurityConstants.RESET_PASSWORD_EXP_TIME);
		Date date = Date.from(days.atStartOfDay(ZoneId.systemDefault()).toInstant());

		PasswordResets passwordResets = new PasswordResets()
				.setEmail(passwordResetDTO.getEmail()).setToken(token).setExpiryDate(date);

		passwordResetsRepository.save(passwordResets);

		return "Check your emails for confirmation link";

	}

	@Override
	public String verifyEmailToken(PasswordResetsDTO passwordResetDTO)
			throws ResetPasswordTokenInvalidException, PasswordsDontMatchException
	{
		if (!passwordResetDTO.getNewPassword().equals(passwordResetDTO.getConfirmPassword()))
		{
			throw new PasswordsDontMatchException("Passwords do not match");
		}

		// Hash password
		passwordResetDTO.setNewPassword(passwordEncoder.encode(passwordResetDTO.getNewPassword()));

		Optional<PasswordResets> passwordReset = passwordResetsRepository
				.findByToken(passwordResetDTO.getToken());
		if (passwordReset.isEmpty())
		{
			throw new ResetPasswordTokenInvalidException("Token is invalid");
		}
		final Date expiration = passwordReset.get().getExpiryDate();

		/*
		 * If true, token expired, throw new exception, else proceed
		 */
		if (expiration.before(new Date(System.currentTimeMillis())))
		{
			throw new ResetPasswordTokenInvalidException("Token has expired");

		} else
		{
			if (changeUserPassword(passwordReset.get(), passwordResetDTO.getNewPassword()))
			{
				passwordResetsRepository.deleteById(passwordReset.get().getId());
				return "Password changed successfully";
			} else
			{
				return String.format("User with given email: '%s' does not exist",
						passwordReset.get().getEmail());
			}
		}

	}

	public boolean changeUserPassword(PasswordResets passwordReset, String newPassword)
	{
		Optional<User> user;
		try
		{
			user = userService.getByEmail(passwordReset.getEmail());
			userService
					.updatePassword(
							new UserDTO().setPassword(newPassword).setId(user.get().getId()));
			return true;
			
		} catch (UserException e)
		{
			return false;
		}

	}

}
