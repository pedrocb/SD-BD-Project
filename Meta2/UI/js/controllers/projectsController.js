myApp.controller('projectsController', ['$scope', '$log', '$http', '$location', function($scope, $log, $http, $location){
	$http.get('http://localhost:8080/api/getCurrentProjects').success(function(result){
		$scope.currentProjects = result.projects;
		$log.info($scope.currentProjects);
	});
	$http.get('http://localhost:8080/api/getOlderProjects').success(function(result){
		$scope.olderProjects = result.projects;
		$log.info($scope.olderProjects);
	});

	$scope.showProject = function(project){
		$location.path('/projects/' + project.id).replace();
	}
}])