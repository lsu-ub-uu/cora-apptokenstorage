import se.uu.ub.cora.apptokenstorage.AppTokenStorageViewInstanceProviderImp;
import se.uu.ub.cora.apptokenverifier.AppTokenStorageViewInstanceProvider;

module se.uu.ub.cora.apptokenstorage {
	requires se.uu.ub.cora.logger;
	requires transitive se.uu.ub.cora.storage;
	requires transitive se.uu.ub.cora.apptokenverifier;
	requires transitive se.uu.ub.cora.spider;

	provides AppTokenStorageViewInstanceProvider with AppTokenStorageViewInstanceProviderImp;

	exports se.uu.ub.cora.apptokenstorage;
}