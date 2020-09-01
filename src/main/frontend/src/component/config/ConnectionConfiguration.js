import React, { Fragment } from 'react';
import Button, { ButtonGroup } from '@atlaskit/button';
import TextField from '@atlaskit/textfield';
import Form, {
  ErrorMessage,
  Field,
  FormFooter,
  HelperMessage,
  ValidMessage,
} from '@atlaskit/form';

const ConnectionConfiguration = () => {

  return (
    <div style={{
      width: '400px',
      maxWidth: '100%',
    }}>
      <Form onSubmit={data => {
        console.log('form data', data);
      }}>
        {({ formProps, submitting }) => (
          <form {...formProps}>
            <Field
              name="email"
              label="Email"
              isRequired
              validate={value => validateEmail(value)}
            >
              {({ fieldProps, error }) => (
                <Fragment>
                  <TextField autoComplete="off" {...fieldProps} />
                  {!error && (
                    <HelperMessage>
                      Use the email you use to authenticate with Atlassian
                      Marketplace
                    </HelperMessage>
                  )}
                  {error && (<ErrorMessage>Email is not valid</ErrorMessage>)}
                </Fragment>
              )}
            </Field>
            <Field
              name="password"
              label="Password"
              isRequired
              validate={value =>
                value && value.length < 8 ? 'TOO_SHORT' : undefined
              }>
              {({ fieldProps, error, valid, meta }) => {
                return (
                  <Fragment>
                    <TextField type="password" {...fieldProps} />
                    {error && !valid && (
                      <HelperMessage>
                        Use 8 or more characters with a mix of letters, numbers
                        & symbols.
                      </HelperMessage>
                    )}
                    {error && (
                      <ErrorMessage>
                        Password needs to be more than 8 characters.
                      </ErrorMessage>
                    )}
                    {valid && meta.dirty ? (
                      <ValidMessage>Awesome password!</ValidMessage>
                    ) : null}
                  </Fragment>
                );
              }}
            </Field>
            <FormFooter>
              <ButtonGroup>
                <Button
                  type="submit"
                  appearance="primary"
                  isLoading={submitting}
                >
                  Save
                </Button>
              </ButtonGroup>
            </FormFooter>
          </form>
        )}
      </Form>
    </div>
  );
};

const validateEmail = (value) => value !== 'nguyentatnhac@gmai.com';

export default ConnectionConfiguration;
