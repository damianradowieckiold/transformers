package com.radowiecki.transformers.security;

import javax.sql.DataSource;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.provisioning.JdbcUserDetailsManagerConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer.AuthorizedUrl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
	private final Logger logger = Logger.getLogger(SecurityConfiguration.class);
	@Autowired
	private DataSource dataSource;

	@Autowired
	public void configAuthentication(AuthenticationManagerBuilder auth) throws Exception {
		((JdbcUserDetailsManagerConfigurer) auth.jdbcAuthentication().dataSource(this.dataSource)
				.usersByUsernameQuery("select username,password, enabled from users where username=?")
				.passwordEncoder(new BCryptPasswordEncoder())).authoritiesByUsernameQuery(
						"select username, authority from users, authorities where username=? and users.authorityId = authorities.id");
	}

	protected void configure(HttpSecurity http) throws Exception {
		((HttpSecurity) ((FormLoginConfigurer) ((FormLoginConfigurer) ((FormLoginConfigurer) ((FormLoginConfigurer) ((HttpSecurity) ((AuthorizedUrl) ((AuthorizedUrl) ((AuthorizedUrl) ((AuthorizedUrl) ((AuthorizedUrl) ((AuthorizedUrl) ((AuthorizedUrl) ((AuthorizedUrl) ((AuthorizedUrl) ((AuthorizedUrl) ((AuthorizedUrl) ((AuthorizedUrl) ((AuthorizedUrl) ((AuthorizedUrl) ((AuthorizedUrl) ((AuthorizedUrl) ((AuthorizedUrl) ((AuthorizedUrl) ((AuthorizedUrl) ((AuthorizedUrl) ((AuthorizedUrl) ((AuthorizedUrl) ((AuthorizedUrl) ((AuthorizedUrl) ((AuthorizedUrl) ((AuthorizedUrl) ((AuthorizedUrl) ((AuthorizedUrl) ((AuthorizedUrl) ((AuthorizedUrl) ((AuthorizedUrl) http
				.authorizeRequests().antMatchers(new String[] { "/" })).permitAll()
						.antMatchers(new String[] { "/registerUser" })).permitAll()
								.antMatchers(new String[] { "/registration" })).permitAll().antMatchers(new String[] {
										"/registrationResult" })).permitAll().antMatchers(new String[] { "/res/**" }))
												.permitAll().antMatchers(new String[] { "/changeLanguage" }))
														.permitAll().antMatchers(new String[] { "/scheduledTasks/**" }))
																.authenticated()
																.antMatchers(new String[] { "/planet/chosenPlanet" }))
																		.authenticated()
																		.antMatchers(new String[] { "/attack" }))
																				.authenticated().antMatchers(
																						new String[] { "/fleetTypes" }))
																								.authenticated()
																								.antMatchers(
																										new String[] {
																												"/fleetBuilding" }))
																														.authenticated()
																														.antMatchers(
																																new String[] {
																																		"/fleetsBuilding" }))
																																				.authenticated()
																																				.antMatchers(
																																						new String[] {
																																								"/userFleets" }))
																																										.authenticated()
																																										.antMatchers(
																																												new String[] {
																																														"/userResources" }))
																																																.authenticated()
																																																.antMatchers(
																																																		new String[] {
																																																				"/isFleetBuildingTaskFinished" }))
																																																						.authenticated()
																																																						.antMatchers(
																																																								new String[] {
																																																										"/userFleetFactories" }))
																																																												.authenticated()
																																																												.antMatchers(
																																																														new String[] {
																																																																"/isFleetFactoryBuildingTaskFinished" }))
																																																																		.authenticated()
																																																																		.antMatchers(
																																																																				new String[] {
																																																																						"/fleetFactoriesBuilding" }))
																																																																								.authenticated()
																																																																								.antMatchers(
																																																																										new String[] {
																																																																												"/fleetFactoryBuilding" }))
																																																																														.authenticated()
																																																																														.antMatchers(
																																																																																new String[] {
																																																																																		"/resourceFactoriesBuilding" }))
																																																																																				.authenticated()
																																																																																				.antMatchers(
																																																																																						new String[] {
																																																																																								"/resourceFactoryBuilding" }))
																																																																																										.authenticated()
																																																																																										.antMatchers(
																																																																																												new String[] {
																																																																																														"/isResourceFactoryBuildingTaskFinished" }))
																																																																																																.authenticated()
																																																																																																.antMatchers(
																																																																																																		new String[] {
																																																																																																				"/userResourceFactories" }))
																																																																																																						.authenticated()
																																																																																																						.antMatchers(
																																																																																																								new String[] {
																																																																																																										"/resourceTypes" }))
																																																																																																												.authenticated()
																																																																																																												.antMatchers(
																																																																																																														new String[] {
																																																																																																																"/resourceStorageBuilding" }))
																																																																																																																		.authenticated()
																																																																																																																		.antMatchers(
																																																																																																																				new String[] {
																																																																																																																						"/resourceStoragesBuilding" }))
																																																																																																																								.authenticated()
																																																																																																																								.antMatchers(
																																																																																																																										new String[] {
																																																																																																																												"/isResourceStorageBuildingTaskFinished" }))
																																																																																																																														.authenticated()
																																																																																																																														.antMatchers(
																																																																																																																																new String[] {
																																																																																																																																		"/userResourceStorages" }))
																																																																																																																																				.authenticated()
																																																																																																																																				.antMatchers(
																																																																																																																																						new String[] {
																																																																																																																																								"/isAttackTaskFinished" }))
																																																																																																																																										.authenticated()
																																																																																																																																										.antMatchers(
																																																																																																																																												new String[] {
																																																																																																																																														"/game" }))
																																																																																																																																																.authenticated()
																																																																																																																																																.antMatchers(
																																																																																																																																																		new String[] {
																																																																																																																																																				"/**" }))
																																																																																																																																																						.denyAll()
																																																																																																																																																						.and()).formLogin()
																																																																																																																																																								.loginPage(
																																																																																																																																																										"/login")
																																																																																																																																																								.permitAll())
																																																																																																																																																										.failureUrl(
																																																																																																																																																												"/login?error=true"))
																																																																																																																																																														.loginProcessingUrl(
																																																																																																																																																																"/j_spring_security_check"))
																																																																																																																																																																		.usernameParameter(
																																																																																																																																																																				"username")
																																																																																																																																																																		.passwordParameter(
																																																																																																																																																																				"password")
																																																																																																																																																																		.successHandler(
																																																																																																																																																																				(request,
																																																																																																																																																																						response,
																																																																																																																																																																						authentication) -> {
																																																																																																																																																																					this.logger
																																																																																																																																																																							.info("User "
																																																																																																																																																																									+ authentication
																																																																																																																																																																											.getName()
																																																																																																																																																																									+ " has logged in.");
																																																																																																																																																																					response.sendRedirect(
																																																																																																																																																																							"/Transformers/game");
																																																																																																																																																																				})).and())
																																																																																																																																																																						.logout()
																																																																																																																																																																						.logoutSuccessHandler(
																																																																																																																																																																								(request,
																																																																																																																																																																										response,
																																																																																																																																																																										authentication) -> {
																																																																																																																																																																									this.logger
																																																																																																																																																																											.info("User "
																																																																																																																																																																													+ authentication
																																																																																																																																																																															.getName()
																																																																																																																																																																													+ " has logged out.");
																																																																																																																																																																									response.sendRedirect(
																																																																																																																																																																											"/Transformers/");
																																																																																																																																																																								});
	}
}